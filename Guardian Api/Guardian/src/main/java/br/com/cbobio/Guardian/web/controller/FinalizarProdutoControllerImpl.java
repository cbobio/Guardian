/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.MovimentacaoProdutoService;
import com.atacadao.Guardian.web.controller.endpoint.IFinalizarProdutoController;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FinalizarProdutoControllerImpl
        implements IFinalizarProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> finalizarProduto(
            Long usuarioId,
            Long movimentacaoId,
            String observacao
    ) {
        String mensagem = movimentacaoProdutoService.finalizarProduto(
                usuarioId,
                movimentacaoId,
                observacao
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(mensagem)
                )
        );
    }
}