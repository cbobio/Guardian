/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.guard.entregaGuard;

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
public class EntregarProdutoGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    public boolean evaluate(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        log.info(">>> EntregarProdutoGuard EXECUTADA");

        if (context.getMessage() == null) {
            log.warn(">>> BLOQUEADO: mensagem não encontrada");
            return false;
        }

        Long usuarioId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long retiradaId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.RETIRADA_ID, Long.class);

        Long produtoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.PRODUTO_ID, Long.class);

        Long lojaId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.LOJA_ID, Long.class);

        Integer quantidade = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.QUANTIDADE, Integer.class);

        if (usuarioId == null
                || retiradaId == null
                || produtoId == null
                || lojaId == null
                || quantidade == null) {

            log.warn(">>> BLOQUEADO: parâmetros obrigatórios ausentes");
            return false;
        }

        if (quantidade <= 0) {
            log.warn(">>> BLOQUEADO: quantidade inválida: {}", quantidade);
            return false;
        }

        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null
                || !Boolean.TRUE.equals(usuario.getAtivo())
                || Boolean.TRUE.equals(usuario.getBloqueado())) {

            log.warn(">>> BLOQUEADO: usuário inválido, inativo ou bloqueado");
            return false;
        }

        TbLoja loja = lojaRepository.findById(lojaId)
                .orElse(null);

        if (loja == null || !Boolean.TRUE.equals(loja.getAtivo())) {
            log.warn(">>> BLOQUEADO: loja inválida ou inativa");
            return false;
        }

        TbProduto produto = produtoRepository.findById(produtoId)
                .orElse(null);

        if (produto == null
                || !Boolean.TRUE.equals(produto.getAtivo())
                || !Boolean.TRUE.equals(produto.getAltoRisco())) {

            log.warn(">>> BLOQUEADO: produto inválido, inativo ou não é alto risco");
            return false;
        }

        TbMovimentacaoProduto retirada =
                movimentacaoProdutoRepository.findById(retiradaId)
                        .orElse(null);

        if (retirada == null
                || retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {

            log.warn(
                    ">>> BLOQUEADO: retirada inválida ou inexistente. ID: {}",
                    retiradaId
            );
            return false;
        }

        if (!retirada.getProduto().getId().equals(produtoId)
                || !retirada.getLoja().getId().equals(lojaId)) {

            log.warn(
                    ">>> BLOQUEADO: produto ou loja não correspondem à retirada {}",
                    retiradaId
            );
            return false;
        }

        if (quantidade > retirada.getQuantidade()) {
            log.warn(
                    ">>> BLOQUEADO: quantidade solicitada ({}) excede a retirada ({})",
                    quantidade,
                    retirada.getQuantidade()
            );
            return false;
        }

        boolean jaFoiEntregue =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retiradaId,
                        MachineEventos.ENTREGAR
                );

        if (jaFoiEntregue) {
            log.warn(
                    ">>> BLOQUEADO: retirada {} já possui entrega registrada",
                    retiradaId
            );
            return false;
        }

        log.info(
                ">>> GUARD APROVADA: entrega autorizada para retirada {}",
                retiradaId
        );

        return true;
    }
}