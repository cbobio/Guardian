/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.action;

import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
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
public class RetornarEstoqueAction
        implements Action<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void execute(StateContext<MachineEstados, MachineEventos> context) {

        log.info("Iniciando retorno de produto ao estoque.");

        try {
            if (context.getMessage() == null) {
                throw new IllegalArgumentException(
                        "A mensagem do evento RETORNAR_ESTOQUE não foi informada."
                );
            }

            Long usuarioId = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.USUARIO_ID, Long.class);

            Long movimentacaoId = context.getMessage()
                    .getHeaders()
                    .get(MachineHeaders.MOVIMENTACAO_ID, Long.class);

            if (usuarioId == null || movimentacaoId == null) {
                throw new IllegalArgumentException(
                        "O usuário e a movimentação são obrigatórios para retornar ao estoque."
                );
            }

            log.debug(
                    "Processando retorno ao estoque. usuarioId={}, movimentacaoId={}",
                    usuarioId,
                    movimentacaoId
            );

            TbUsuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário não encontrado: " + usuarioId
                    ));

            TbMovimentacaoProduto origem = movimentacaoProdutoRepository
                    .buscarPorIdComBloqueio(movimentacaoId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Movimentação não encontrada: " + movimentacaoId
                    ));

            log.debug(
                    "Movimentação de origem encontrada. id={}, evento={}, estado={}, quantidade={}",
                    origem.getId(),
                    origem.getEvento(),
                    origem.getEstado(),
                    origem.getQuantidade()
            );

            if (origem.getEvento() != MachineEventos.DEVOLVER
                    || origem.getEstado() != MachineEstados.DEVOLVIDO) {
                throw new IllegalArgumentException(
                        "Somente uma movimentação de devolução no estado DEVOLVIDO "
                                + "pode retornar ao estoque."
                );
            }

            TbMovimentacaoProduto retorno = TbMovimentacaoProduto.builder()
                    .evento(MachineEventos.RETORNAR_ESTOQUE)
                    .estado(MachineEstados.ESTOQUE)
                    .estadoAnterior(MachineEstados.DEVOLVIDO)
                    .motivo(origem.getMotivo())
                    .quantidade(origem.getQuantidade())
                    .produto(origem.getProduto())
                    .loja(origem.getLoja())
                    .usuario(usuario)
                    .movimentacaoOrigem(origem)
                    .observacao("Retorno ao estoque após devolução")
                    .build();

            TbMovimentacaoProduto salvo =
                    movimentacaoProdutoRepository.saveAndFlush(retorno);

            log.info(
                    "Retorno ao estoque registrado com sucesso. id={}, origemId={}, quantidade={}",
                    salvo.getId(),
                    movimentacaoId,
                    salvo.getQuantidade()
            );

        } catch (RuntimeException e) {
            log.error(
                    "Falha ao retornar produto ao estoque. Tipo={}, mensagem={}",
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }
}