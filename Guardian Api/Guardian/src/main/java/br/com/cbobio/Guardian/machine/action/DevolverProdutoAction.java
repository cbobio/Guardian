/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.action;

import com.atacadao.Guardian.enums.MotivoMovimentacao;
import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import com.atacadao.Guardian.model.repository.EstoqueRepository;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DevolverProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void execute(StateContext<MachineEstados, MachineEventos> context) {

        try {
            log.info("Iniciando DevolverProdutoAction.");

            Long usuarioId = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.USUARIO_ID, Long.class);

            Long movimentacaoId = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.MOVIMENTACAO_ID, Long.class);

            Integer quantidade = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.QUANTIDADE, Integer.class);

            String observacao = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.OBSERVACAO, String.class);

            log.info(
                    "Dados da devolução: usuário={}, movimentação={}, quantidade={}",
                    usuarioId,
                    movimentacaoId,
                    quantidade
            );

            if (usuarioId == null
                    || movimentacaoId == null
                    || quantidade == null
                    || quantidade <= 0) {

                throw new IllegalArgumentException(
                        "Os dados obrigatórios da devolução são inválidos."
                );
            }

            TbUsuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário não encontrado: " + usuarioId
                    ));

            log.info(
                    "Usuário encontrado para devolução: {}",
                    usuarioId
            );

            TbMovimentacaoProduto origem =
                    movimentacaoProdutoRepository.buscarPorIdComBloqueio(
                                    movimentacaoId
                            )
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Movimentação de origem não encontrada: "
                                            + movimentacaoId
                            ));

            if (origem.getEvento() != MachineEventos.RETIRAR
                    && origem.getEvento() != MachineEventos.ENTREGAR) {

                throw new IllegalArgumentException(
                        "A movimentação de origem não é uma retirada ou entrega válida."
                );
            }

            if (origem.getEstado() != MachineEstados.RETIRADO
                    && origem.getEstado() != MachineEstados.ENTREGUE) {

                throw new IllegalArgumentException(
                        "O estado da movimentação não permite devolução."
                );
            }

            Integer quantidadeOriginal = origem.getQuantidade();

            if (quantidadeOriginal == null || quantidadeOriginal <= 0) {
                throw new IllegalArgumentException(
                        "A movimentação de origem não possui quantidade válida."
                );
            }

            Long quantidadeJaDevolvida =
                    movimentacaoProdutoRepository
                            .somarQuantidadePorOrigemEEvento(
                                    movimentacaoId,
                                    MachineEventos.DEVOLVER
                            );

            if (quantidadeJaDevolvida == null) {
                quantidadeJaDevolvida = 0L;
            }

            long quantidadeDisponivel = Math.max(
                    0L,
                    quantidadeOriginal.longValue() - quantidadeJaDevolvida
            );

            log.info(
                    "Saldo para devolução: original={}, devolvido={}, disponível={}, solicitado={}",
                    quantidadeOriginal,
                    quantidadeJaDevolvida,
                    quantidadeDisponivel,
                    quantidade
            );

            if (quantidade > quantidadeDisponivel) {
                throw new IllegalArgumentException(
                        "Quantidade solicitada excede o saldo disponível para devolução. "
                                + "Quantidade disponível: " + quantidadeDisponivel + "."
                );
            }

            TbEstoque estoque = estoqueRepository
                    .findByLojaIdAndProdutoId(
                            origem.getLoja().getId(),
                            origem.getProduto().getId()
                    )
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Estoque não encontrado para o produto "
                                    + origem.getProduto().getId()
                                    + " na loja "
                                    + origem.getLoja().getId()
                    ));

            log.info(
                    "Estoque encontrado. Loja={}, produto={}, quantidade atual={}",
                    origem.getLoja().getId(),
                    origem.getProduto().getId(),
                    estoque.getQuantidade()
            );

            TbMovimentacaoProduto devolucao = TbMovimentacaoProduto.builder()
                    .evento(MachineEventos.DEVOLVER)
                    .estado(MachineEstados.DEVOLVIDO)
                    .estadoAnterior(origem.getEstado())
                    .motivo(MotivoMovimentacao.DEVOLUCAO_CLIENTE)
                    .quantidade(quantidade)
                    .observacao(observacao)
                    .produto(origem.getProduto())
                    .loja(origem.getLoja())
                    .usuario(usuario)
                    .movimentacaoOrigem(origem)
                    .build();

            movimentacaoProdutoRepository.saveAndFlush(devolucao);

            log.info(
                    "Movimentação de devolução salva. ID={}, origem={}, quantidade={}",
                    devolucao.getId(),
                    movimentacaoId,
                    quantidade
            );

            estoque.setQuantidade(
                    estoque.getQuantidade() + quantidade
            );

            estoqueRepository.saveAndFlush(estoque);

            log.info(
                    "Estoque atualizado. Loja={}, produto={}, nova quantidade={}",
                    origem.getLoja().getId(),
                    origem.getProduto().getId(),
                    estoque.getQuantidade()
            );

            log.info(
                    "DevolverProdutoAction concluída. Origem={}, quantidade={}, usuário={}",
                    movimentacaoId,
                    quantidade,
                    usuarioId
            );

        } catch (RuntimeException e) {
            log.error(
                    "Erro ao executar DevolverProdutoAction: {}",
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }
}