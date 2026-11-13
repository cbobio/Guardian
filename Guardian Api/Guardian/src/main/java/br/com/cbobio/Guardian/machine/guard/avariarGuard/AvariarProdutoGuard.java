/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.guard.avariarGuard;

import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import com.atacadao.Guardian.model.repository.LojaRepository;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.model.repository.ProdutoRepository;
import com.atacadao.Guardian.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvariarProdutoGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    public boolean evaluate(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        log.info(">>> AvariarProdutoGuard EXECUTADA");

        if (context.getMessage() == null) {
            log.warn(">>> BLOQUEADO: mensagem não encontrada");
            return false;
        }

        Long usuarioId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long movimentacaoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.MOVIMENTACAO_ID, Long.class);

        Integer quantidade = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.QUANTIDADE, Integer.class);

        if (usuarioId == null
                || movimentacaoId == null
                || quantidade == null) {

            log.warn(
                    ">>> BLOQUEADO: usuário, movimentação ou quantidade não informados"
            );
            return false;
        }

        if (quantidade <= 0) {
            log.warn(
                    ">>> BLOQUEADO: quantidade inválida: {}",
                    quantidade
            );
            return false;
        }

        // Valida o usuário
        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null
                || !Boolean.TRUE.equals(usuario.getAtivo())
                || Boolean.TRUE.equals(usuario.getBloqueado())) {

            log.warn(
                    ">>> BLOQUEADO: usuário inválido, inativo ou bloqueado. ID: {}",
                    usuarioId
            );
            return false;
        }

        // Busca a movimentação que será avariada
        TbMovimentacaoProduto movimentacao =
                movimentacaoProdutoRepository.findById(movimentacaoId)
                        .orElse(null);

        if (movimentacao == null) {
            log.warn(
                    ">>> BLOQUEADO: movimentação não encontrada. ID: {}",
                    movimentacaoId
            );
            return false;
        }

        // Permite avaria somente a partir dos estados previstos
        MachineEstados estadoAtual = movimentacao.getEstado();

        if (estadoAtual != MachineEstados.ESTOQUE
                && estadoAtual != MachineEstados.RETIRADO
                && estadoAtual != MachineEstados.ENTREGUE) {

            log.warn(
                    ">>> BLOQUEADO: movimentação {} não pode ser avariada. Estado: {}",
                    movimentacaoId,
                    estadoAtual
            );
            return false;
        }

        // Confere se o estado da movimentação é compatível com seu evento
        boolean eventoCompativel = switch (estadoAtual) {
            case ESTOQUE ->
                    movimentacao.getEvento() == MachineEventos.RETORNAR_ESTOQUE;

            case RETIRADO ->
                    movimentacao.getEvento() == MachineEventos.RETIRAR;

            case ENTREGUE ->
                    movimentacao.getEvento() == MachineEventos.ENTREGAR;

            default -> false;
        };

        if (!eventoCompativel) {
            log.warn(
                    ">>> BLOQUEADO: evento {} incompatível com o estado {} da movimentação {}",
                    movimentacao.getEvento(),
                    estadoAtual,
                    movimentacaoId
            );
            return false;
        }

        // Valida os relacionamentos
        if (movimentacao.getProduto() == null
                || movimentacao.getLoja() == null) {

            log.warn(
                    ">>> BLOQUEADO: movimentação {} sem produto ou loja associados",
                    movimentacaoId
            );
            return false;
        }

        Long produtoId = movimentacao.getProduto().getId();
        Long lojaId = movimentacao.getLoja().getId();

        // Valida o produto
        TbProduto produto = produtoRepository.findById(produtoId)
                .orElse(null);

        if (produto == null
                || !Boolean.TRUE.equals(produto.getAtivo())) {

            log.warn(
                    ">>> BLOQUEADO: produto inexistente ou inativo. ID: {}",
                    produtoId
            );
            return false;
        }

        // Valida a loja
        TbLoja loja = lojaRepository.findById(lojaId)
                .orElse(null);

        if (loja == null || !Boolean.TRUE.equals(loja.getAtivo())) {
            log.warn(
                    ">>> BLOQUEADO: loja inexistente ou inativa. ID: {}",
                    lojaId
            );
            return false;
        }

        // Valida a quantidade registrada na movimentação de origem
        Integer quantidadeOrigem = movimentacao.getQuantidade();

        if (quantidadeOrigem == null || quantidadeOrigem <= 0) {
            log.warn(
                    ">>> BLOQUEADO: quantidade de origem inválida. " +
                            "Movimentação: {}, Quantidade: {}",
                    movimentacaoId,
                    quantidadeOrigem
            );
            return false;
        }

        // Consulta a quantidade total já avariada para esta origem
        Long quantidadeJaAvariada =
                movimentacaoProdutoRepository.somarQuantidadePorOrigemEEvento(
                        movimentacaoId,
                        MachineEventos.AVARIAR
                );

        if (quantidadeJaAvariada == null) {
            quantidadeJaAvariada = 0L;
        }

        long quantidadeDisponivel =
                quantidadeOrigem.longValue() - quantidadeJaAvariada;

        if (quantidade > quantidadeDisponivel) {
            log.warn(
                    ">>> BLOQUEADO: quantidade solicitada excede o saldo disponível. " +
                            "Origem: {}, Quantidade original: {}, Já avariada: {}, " +
                            "Disponível: {}, Solicitada: {}",
                    movimentacaoId,
                    quantidadeOrigem,
                    quantidadeJaAvariada,
                    quantidadeDisponivel,
                    quantidade
            );
            return false;
        }

        log.info(
                ">>> QUANTIDADE VALIDADA: origem {}, original {}, já avariada {}, " +
                        "disponível {}, solicitada {}",
                movimentacaoId,
                quantidadeOrigem,
                quantidadeJaAvariada,
                quantidadeDisponivel,
                quantidade
        );

        return true;
    }
}