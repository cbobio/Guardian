/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller.endpoint;

import com.atacadao.Guardian.web.dto.request.produto.FilterProdutoDto;
import com.atacadao.Guardian.web.dto.request.produto.ProdutoRequestDto;
import com.atacadao.Guardian.web.dto.response.produto.ProdutoResponseDto;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/produtos")
public interface IProdutoController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> cadastrar(
            @Valid @RequestBody ProdutoRequestDto dto
    );

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> buscarPorId(
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<DefaultApiResponse<Page<ProdutoResponseDto>>> listarTodos(
            @ModelAttribute FilterProdutoDto filtro,
            @PageableDefault(
                    size = 10,
                    sort = "nome",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    );

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDto dto
    );

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(
            @PathVariable Long id
    );
}