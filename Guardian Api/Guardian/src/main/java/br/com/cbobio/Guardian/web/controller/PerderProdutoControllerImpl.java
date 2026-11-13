/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.MovimentacaoProdutoService;
import com.atacadao.Guardian.web.controller.endpoint.movimentacoes.IPerderProdutoController;
import com.atacadao.Guardian.web.dto.request.movimentacao.PerderProdutoRequest;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PerderProdutoControllerImpl
        implements IPerderProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> perderProduto(
            PerderProdutoRequest request
    ) {

        movimentacaoProdutoService.perderProduto(
                request.usuarioId(),
                request.movimentacaoId()
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Produto perdido registrado com sucesso."
                        )
                )
        );
    }
}