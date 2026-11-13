/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.guard.perdaGuard;

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
public class PerderProdutoGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    public boolean evaluate(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        log.info(">>> PerderProdutoGuard EXECUTADA");

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

        if (usuarioId == null || movimentacaoId == null) {
            log.warn(">>> BLOQUEADO: usuário ou movimentação não informados");
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

        // Busca a movimentação de entrega
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

        // Valida o evento e o estado atual
        if (movimentacao.getEvento() != MachineEventos.ENTREGAR
                || movimentacao.getEstado() != MachineEstados.ENTREGUE) {

            log.warn(
                    ">>> BLOQUEADO: movimentação {} não está no estado ENTREGUE",
                    movimentacaoId
            );
            return false;
        }

        // Valida os relacionamentos da movimentação
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
                || !Boolean.TRUE.equals(produto.getAtivo())
                || !Boolean.TRUE.equals(produto.getAltoRisco())) {

            log.warn(
                    ">>> BLOQUEADO: produto inválido, inativo ou não é de alto risco. ID: {}",
                    produtoId
            );
            return false;
        }

        // Valida a loja
        TbLoja loja = lojaRepository.findById(lojaId)
                .orElse(null);

        if (loja == null || !Boolean.TRUE.equals(loja.getAtivo())) {
            log.warn(
                    ">>> BLOQUEADO: loja inválida ou inativa. ID: {}",
                    lojaId
            );
            return false;
        }

        // Obtém e valida a retirada original vinculada à entrega
        TbMovimentacaoProduto retirada = movimentacao.getRetirada();

        if (retirada == null
                || retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {

            log.warn(
                    ">>> BLOQUEADO: entrega {} sem retirada de origem válida",
                    movimentacaoId
            );
            return false;
        }

        // Verifica se a retirada já possui uma perda registrada
        boolean jaFoiRegistradaPerda =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retirada.getId(),
                        MachineEventos.PERDER
                );

        if (jaFoiRegistradaPerda) {
            log.warn(
                    ">>> BLOQUEADO: retirada {} já possui perda registrada",
                    retirada.getId()
            );
            return false;
        }

        log.info(
                ">>> GUARD APROVADA: perda autorizada para entrega {} e retirada {}",
                movimentacaoId,
                retirada.getId()
        );

        return true;
    }
}