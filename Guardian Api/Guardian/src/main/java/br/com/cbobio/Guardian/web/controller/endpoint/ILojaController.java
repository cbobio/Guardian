/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller.endpoint;

import com.atacadao.Guardian.web.dto.request.loja.FilterLojaDto;
import com.atacadao.Guardian.web.dto.request.loja.LojaRequestDto;
import com.atacadao.Guardian.web.dto.response.loja.LojaResponseDto;
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
@RequestMapping("/api/v1/lojas")
public interface ILojaController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> cadastrar(@Valid @RequestBody LojaRequestDto dto);

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> buscarPorId(@PathVariable Long id);

    @GetMapping
    ResponseEntity<DefaultApiResponse<Page<LojaResponseDto>>> listarTodos(
            @ModelAttribute FilterLojaDto filtro,
            @PageableDefault(
                    size = 10,
                    sort = "nomeFantasia",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    );

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> atualizar(@PathVariable Long id, @Valid @RequestBody LojaRequestDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(@PathVariable Long id);
}