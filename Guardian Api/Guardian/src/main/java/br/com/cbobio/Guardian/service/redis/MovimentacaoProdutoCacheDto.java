/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service.redis;

import com.atacadao.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;

import java.util.List;

public record MovimentacaoProdutoCacheDto(
        List<MovimentacaoProdutoResponseDto> conteudo,
        int numeroPagina,
        int tamanhoPagina,
        long totalElementos,
        int totalPaginas
) {
}