/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.MovimentacaoProdutoService;
import com.atacadao.Guardian.web.controller.endpoint.movimentacoes.IAvariarProdutoController;
import com.atacadao.Guardian.web.dto.request.movimentacao.AvariarProdutoRequest;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AvariarProdutoControllerImpl
        implements IAvariarProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> avariarProduto(
            AvariarProdutoRequest request
    ) {

        movimentacaoProdutoService.avariarProduto(
                request.usuarioId(),
                request.movimentacaoId(),
                request.quantidade()
        );

        DefaultApiResponse<String> response = DefaultApiResponse.success(
                new ApiResponseData<>(
                        "Avaria do produto registrada com sucesso."
                )
        );

        return ResponseEntity.ok(response);
    }
}