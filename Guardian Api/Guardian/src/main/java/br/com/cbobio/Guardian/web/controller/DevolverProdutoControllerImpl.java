/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;


import com.atacadao.Guardian.service.MovimentacaoProdutoService;
import com.atacadao.Guardian.web.controller.endpoint.movimentacoes.IDevolverProdutoController;
import com.atacadao.Guardian.web.dto.request.movimentacao.DevolverProdutoRequest;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DevolverProdutoControllerImpl implements IDevolverProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> devolverProduto(
            DevolverProdutoRequest request
    ) {
        movimentacaoProdutoService.devolverProduto(
                request.getUsuarioId(),
                request.getMovimentacaoId(),
                request.getQuantidade(),
                request.getObservacao()
        );

        ApiResponseData<String> data = new ApiResponseData<>();
        data.setResource("Devolução de produto registrada com sucesso.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DefaultApiResponse.success(data));
    }
}