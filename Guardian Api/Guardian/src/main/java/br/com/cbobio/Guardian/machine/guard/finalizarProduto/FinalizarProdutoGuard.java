/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.guard.finalizarProduto;

import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.model.repository.UsuarioRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class FinalizarProdutoGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final UsuarioRepository usuarioRepository;

    public FinalizarProdutoGuard(
            MovimentacaoProdutoRepository movimentacaoProdutoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.movimentacaoProdutoRepository = movimentacaoProdutoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean evaluate(
            StateContext<MachineEstados, MachineEventos> context
    ) {
        Message<MachineEventos> message = context.getMessage();

        if (message == null) {
            return false;
        }

        Long usuarioId = message.getHeaders().get(
                MachineHeaders.USUARIO_ID, Long.class
        );

        Long movimentacaoId = message.getHeaders().get(
                MachineHeaders.MOVIMENTACAO_ID, Long.class
        );

        if (usuarioId == null || movimentacaoId == null) {
            return false;
        }

        // 1. Valida a existência e a situação do usuário.
        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null
                || !Boolean.TRUE.equals(usuario.getAtivo())
                || Boolean.TRUE.equals(usuario.getBloqueado())) {
            return false;
        }

        // 2. Busca a movimentação de origem.
        TbMovimentacaoProduto origem =
                movimentacaoProdutoRepository
                        .buscarPorIdComBloqueio(movimentacaoId)
                        .orElse(null);

        if (origem == null) {
            return false;
        }

        // 3. Somente uma entrega concluída pode ser finalizada.
        if (origem.getEvento() != MachineEventos.ENTREGAR
                || origem.getEstado() != MachineEstados.ENTREGUE) {
            return false;
        }

        // 4. Impede uma segunda finalização da mesma entrega.
        return !movimentacaoProdutoRepository
                .existsByMovimentacaoOrigem_IdAndEvento(
                        movimentacaoId,
                        MachineEventos.FINALIZAR
                );
    }
}