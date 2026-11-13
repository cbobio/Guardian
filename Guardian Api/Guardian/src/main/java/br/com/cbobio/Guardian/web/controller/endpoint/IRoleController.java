/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller.endpoint;

import com.atacadao.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import com.atacadao.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/roles")
public interface IRoleController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> cadastrar(
            @Valid @RequestBody RoleRequestDto dto
    );

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> buscarPorId(
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<DefaultApiResponse<List<RoleResponseDto>>> listarTodos();

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDto dto
    );

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(
            @PathVariable Long id
    );
}