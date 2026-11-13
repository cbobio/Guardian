/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes;

import com.atacadao.Guardian.web.dto.request.movimentacao.PerderProdutoRequest;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/movimentacoes")
public interface IPerderProdutoController {

    @PostMapping("/perder")
    ResponseEntity<DefaultApiResponse<String>> perderProduto(
            @Valid @RequestBody PerderProdutoRequest request
    );
}