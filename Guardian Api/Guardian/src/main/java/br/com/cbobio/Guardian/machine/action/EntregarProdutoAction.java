/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.action;

import br.com.cbobio.Guardian.enums.MotivoMovimentacao;
import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import br.com.cbobio.Guardian.machine.guard.MachineHeaders;
import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.model.repository.LojaRepository;
import br.com.cbobio.Guardian.model.repository.MovimentacaoProdutoRepository;
import br.com.cbobio.Guardian.model.repository.ProdutoRepository;
import br.com.cbobio.Guardian.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntregarProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void execute(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        if (context.getMessage() == null) {
            throw new IllegalArgumentException(
                    "Mensagem não informada para entrega do produto."
            );
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

            throw new IllegalArgumentException(
                    "Dados obrigatórios não informados para entrega do produto."
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade para entrega deve ser maior que zero."
            );
        }

        TbMovimentacaoProduto retirada =
                movimentacaoProdutoRepository.findById(retiradaId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Retirada não encontrada: " + retiradaId
                                )
                        );

        if (retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {

            throw new IllegalArgumentException(
                    "A movimentação informada não é uma retirada válida."
            );
        }

        if (!retirada.getProduto().getId().equals(produtoId)
                || !retirada.getLoja().getId().equals(lojaId)) {

            throw new IllegalArgumentException(
                    "O produto ou a loja não correspondem à retirada informada."
            );
        }

        if (quantidade > retirada.getQuantidade()) {
            throw new IllegalArgumentException(
                    "A quantidade entregue não pode exceder a quantidade retirada."
            );
        }

        boolean jaFoiEntregue =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retiradaId,
                        MachineEventos.ENTREGAR
                );

        if (jaFoiEntregue) {
            throw new IllegalStateException(
                    "Esta retirada já possui uma entrega registrada."
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

        TbMovimentacaoProduto movimentacao =
                TbMovimentacaoProduto.builder()
                        .evento(MachineEventos.ENTREGAR)
                        .estado(MachineEstados.ENTREGUE)
                        .estadoAnterior(MachineEstados.RETIRADO)
                        .motivo(MotivoMovimentacao.VENDA)
                        .quantidade(quantidade)
                        .produto(produto)
                        .loja(loja)
                        .usuario(usuario)
                        .retirada(retirada)
                        .build();

        movimentacaoProdutoRepository.saveAndFlush(movimentacao);

        log.info(
                "Produto entregue. Retirada: {}, produto: {}, loja: {}, quantidade: {}",
                retiradaId,
                produtoId,
                lojaId,
                quantidade
        );
    }
}