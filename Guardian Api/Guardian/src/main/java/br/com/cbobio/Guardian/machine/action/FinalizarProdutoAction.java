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
import com.atacadao.Guardian.service.exception.ConflitoException;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FinalizarProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final UsuarioRepository usuarioRepository;

    public FinalizarProdutoAction(
            MovimentacaoProdutoRepository movimentacaoProdutoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.movimentacaoProdutoRepository = movimentacaoProdutoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void execute(
            StateContext<MachineEstados, MachineEventos> context
    ) {
        Message<MachineEventos> message = context.getMessage();

        if (message == null) {
            throw new IllegalStateException(
                    "Mensagem não encontrada para finalizar a movimentação."
            );
        }

        Long usuarioId = message.getHeaders().get(
                MachineHeaders.USUARIO_ID, Long.class
        );

        Long movimentacaoId = message.getHeaders().get(
                MachineHeaders.MOVIMENTACAO_ID, Long.class
        );

        String observacao = message.getHeaders().get(
                MachineHeaders.OBSERVACAO, String.class
        );

        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "Usuário e movimentação são obrigatórios para finalizar."
            );
        }

        // Valida o usuário responsável pela finalização.
        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário responsável pela finalização não encontrado."
                ));

        if (!Boolean.TRUE.equals(usuario.getAtivo())
                || Boolean.TRUE.equals(usuario.getBloqueado())) {
            throw new IllegalStateException(
                    "Usuário inativo ou bloqueado não pode finalizar a movimentação."
            );
        }

        // Busca a entrega original com bloqueio.
        TbMovimentacaoProduto origem =
                movimentacaoProdutoRepository
                        .buscarPorIdComBloqueio(movimentacaoId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Movimentação de entrega não encontrada."
                        ));

        // Valida o evento e o estado da entrega.
        if (origem.getEvento() != MachineEventos.ENTREGAR
                || origem.getEstado() != MachineEstados.ENTREGUE) {
            throw new IllegalStateException(
                    "Somente uma entrega concluída pode ser finalizada."
            );
        }

        // Impede uma segunda finalização da mesma entrega.
        boolean jaFinalizada =
                movimentacaoProdutoRepository
                        .existsByMovimentacaoOrigem_IdAndEvento(
                                movimentacaoId,
                                MachineEventos.FINALIZAR
                        );

        if (jaFinalizada) {
            throw new ConflitoException(
                    "Esta entrega já possui uma finalização registrada."
            );
        }

        // Cria uma nova movimentação de finalização.
        TbMovimentacaoProduto finalizacao = new TbMovimentacaoProduto();

        finalizacao.setEvento(MachineEventos.FINALIZAR);
        finalizacao.setEstado(MachineEstados.FINALIZADO);
        finalizacao.setEstadoAnterior(MachineEstados.ENTREGUE);

        // Registra quem executou a finalização.
        finalizacao.setUsuario(usuario);

        // Preserva os dados da entrega original.
        finalizacao.setProduto(origem.getProduto());
        finalizacao.setLoja(origem.getLoja());
        finalizacao.setQuantidade(origem.getQuantidade());
        finalizacao.setMotivo(origem.getMotivo());
        finalizacao.setObservacao(observacao);

        // Vincula a finalização à entrega original.
        finalizacao.setMovimentacaoOrigem(origem);

        movimentacaoProdutoRepository.save(finalizacao);
    }
}