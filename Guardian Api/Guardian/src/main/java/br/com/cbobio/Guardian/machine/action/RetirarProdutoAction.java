/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

/*package com.atacadao.Guardian.machine.action;

import com.atacadao.Guardian.enums.MotivoMovimentacao;
import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import com.atacadao.Guardian.model.repository.EstoqueRepository;
import com.atacadao.Guardian.model.repository.LojaRepository;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.model.repository.ProdutoRepository;
import com.atacadao.Guardian.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RetirarProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void execute(StateContext<MachineEstados, MachineEventos> context) {

        Long usuarioId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long produtoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.PRODUTO_ID, Long.class);

        Long lojaId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.LOJA_ID, Long.class);

        Integer quantidade = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.QUANTIDADE, Integer.class);

        if (usuarioId == null ||
                produtoId == null ||
                lojaId == null ||
                quantidade == null) {

            throw new IllegalArgumentException(
                    "Dados obrigatórios não informados para retirada do produto."
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }

        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuário não encontrado."
                        )
                );

        TbProduto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );

        TbLoja loja = lojaRepository.findById(lojaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Loja não encontrada."
                        )
                );

        TbEstoque estoque = estoqueRepository
                .findByLojaIdAndProdutoId(lojaId, produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não possui estoque cadastrado para esta loja."
                        )
                );

        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalArgumentException(
                    "Quantidade em estoque insuficiente."
            );
        }

        MachineEstados estadoAnterior = MachineEstados.ESTOQUE;

        estoque.setQuantidade(
                estoque.getQuantidade() - quantidade
        );

        estoqueRepository.save(estoque);

        TbMovimentacaoProduto movimentacao =
                TbMovimentacaoProduto.builder()
                        .evento(MachineEventos.RETIRAR)
                        .estado(MachineEstados.RETIRADO)
                        .estadoAnterior(estadoAnterior)
                        .motivo(MotivoMovimentacao.VENDA)
                        .quantidade(quantidade)
                        .produto(produto)
                        .loja(loja)
                        .usuario(usuario)
                        .build();

        movimentacaoProdutoRepository.save(movimentacao);
    }
}
*/


/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.action;

import com.atacadao.Guardian.enums.MotivoMovimentacao;
import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import com.atacadao.Guardian.model.repository.EstoqueRepository;
import com.atacadao.Guardian.model.repository.LojaRepository;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.model.repository.ProdutoRepository;
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
public class RetirarProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void execute(StateContext<MachineEstados, MachineEventos> context) {

        log.info(">>> RetirarProdutoAction EXECUTADA");

        Long usuarioId = context.getMessage().getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long produtoId = context.getMessage().getHeaders()
                .get(MachineHeaders.PRODUTO_ID, Long.class);

        Long lojaId = context.getMessage().getHeaders()
                .get(MachineHeaders.LOJA_ID, Long.class);

        Integer quantidade = context.getMessage().getHeaders()
                .get(MachineHeaders.QUANTIDADE, Integer.class);

        log.info(
                "Dados da retirada: usuarioId={}, produtoId={}, lojaId={}, quantidade={}",
                usuarioId, produtoId, lojaId, quantidade
        );

        if (usuarioId == null || produtoId == null
                || lojaId == null || quantidade == null) {
            throw new IllegalArgumentException(
                    "Dados obrigatórios não informados para retirada do produto."
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }

        TbUsuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado."));

        TbProduto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado."));

        TbLoja loja = lojaRepository.findById(lojaId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Loja não encontrada."));

        TbEstoque estoque = estoqueRepository
                .findByLojaIdAndProdutoId(lojaId, produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não possui estoque cadastrado para esta loja."
                        ));

        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalArgumentException(
                    "Quantidade em estoque insuficiente."
            );
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        estoqueRepository.saveAndFlush(estoque);

        log.info(
                ">>> Estoque atualizado. ID={}, nova quantidade={}",
                estoque.getId(), estoque.getQuantidade()
        );

        TbMovimentacaoProduto movimentacao = TbMovimentacaoProduto.builder()
                .evento(MachineEventos.RETIRAR)
                .estado(MachineEstados.RETIRADO)
                .estadoAnterior(MachineEstados.ESTOQUE)
                .motivo(MotivoMovimentacao.VENDA)
                .quantidade(quantidade)
                .produto(produto)
                .loja(loja)
                .usuario(usuario)
                .build();

        TbMovimentacaoProduto salva =
                movimentacaoProdutoRepository.saveAndFlush(movimentacao);

        log.info(
                ">>> Movimentação persistida. ID={}",
                salva.getId()
        );
    }
}
