/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.action;

import com.atacadao.Guardian.enums.MotivoMovimentacao;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvariarProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void execute(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        log.info(">>> AvariarProdutoAction EXECUTADA");

        if (context.getMessage() == null) {
            throw new IllegalStateException(
                    "Mensagem da avaria não encontrada."
            );
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
                || quantidade == null
                || quantidade <= 0) {

            throw new IllegalArgumentException(
                    "Usuário, movimentação e quantidade válidos são obrigatórios."
            );
        }

        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário não encontrado: " + usuarioId
                ));

        if (!Boolean.TRUE.equals(usuario.getAtivo())
                || Boolean.TRUE.equals(usuario.getBloqueado())) {

            throw new IllegalStateException(
                    "O usuário está inativo ou bloqueado."
            );
        }

        TbMovimentacaoProduto origem =
                movimentacaoProdutoRepository.findById(movimentacaoId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Movimentação de origem não encontrada: "
                                        + movimentacaoId
                        ));

        MachineEstados estadoAnterior = origem.getEstado();

        if (estadoAnterior != MachineEstados.ESTOQUE
                && estadoAnterior != MachineEstados.RETIRADO
                && estadoAnterior != MachineEstados.ENTREGUE) {

            throw new IllegalStateException(
                    "A movimentação não está em um estado que permita avaria."
            );
        }

        if (origem.getQuantidade() == null
                || quantidade > origem.getQuantidade()) {

            throw new IllegalArgumentException(
                    "A quantidade avariada excede a quantidade da movimentação de origem."
            );
        }

        if (origem.getProduto() == null
                || origem.getLoja() == null) {

            throw new IllegalStateException(
                    "A movimentação de origem não possui produto ou loja associados."
            );
        }

        TbMovimentacaoProduto avaria = TbMovimentacaoProduto.builder()
                .evento(MachineEventos.AVARIAR)
                .estado(MachineEstados.AVARIADO)
                .estadoAnterior(estadoAnterior)
                .motivo(MotivoMovimentacao.AVARIA)
                .quantidade(quantidade)
                .produto(origem.getProduto())
                .loja(origem.getLoja())
                .usuario(usuario)
                .movimentacaoOrigem(origem)
                .build();

        movimentacaoProdutoRepository.saveAndFlush(avaria);

        log.info(
                ">>> AVARIA REGISTRADA: origem={}, produto={}, loja={}, usuario={}, quantidade={}, estadoAnterior={}",
                movimentacaoId,
                origem.getProduto().getId(),
                origem.getLoja().getId(),
                usuarioId,
                quantidade,
                estadoAnterior
        );
    }
}