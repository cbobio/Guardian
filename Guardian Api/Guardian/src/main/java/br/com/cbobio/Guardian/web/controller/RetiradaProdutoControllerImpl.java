/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.MovimentacaoProdutoService;
import com.atacadao.Guardian.web.controller.endpoint.movimentacoes.IRetiradaProdutoController;
import com.atacadao.Guardian.web.dto.request.movimentacao.EntregarProdutoRequest;
import com.atacadao.Guardian.web.dto.request.movimentacao.RetiradaProdutoRequest;
import com.atacadao.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RetiradaProdutoControllerImpl
        implements IRetiradaProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> retirarProduto(
            RetiradaProdutoRequest request
    ) {

        Long usuarioId = 1L;

        movimentacaoProdutoService.retirarProduto(
                usuarioId,
                request.getProdutoId(),
                request.getLojaId(),
                request.getQuantidade()
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Produto retirado com sucesso."
                        )
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> entregarProduto(
            EntregarProdutoRequest request
    ) {

        Long usuarioId = 1L;

        movimentacaoProdutoService.entregarProduto(
                usuarioId,
                request.getRetiradaId(),
                request.getProdutoId(),
                request.getLojaId(),
                request.getQuantidade()
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Produto entregue com sucesso."
                        )
                )
        );
    }

    @Override
    public ResponseEntity<
            DefaultApiResponse<Page<MovimentacaoProdutoResponseDto>>
            > listarTodos(Pageable pageable) {

        Page<MovimentacaoProdutoResponseDto> response =
                movimentacaoProdutoService.listarTodos(pageable);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

}