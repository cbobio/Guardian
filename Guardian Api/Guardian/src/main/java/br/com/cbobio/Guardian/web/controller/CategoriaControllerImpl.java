/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.CategoriaService;
import com.atacadao.Guardian.web.controller.endpoint.ICategoriaController;
import com.atacadao.Guardian.web.dto.request.categoria.CategoriaRequestDto;
import com.atacadao.Guardian.web.dto.response.categoria.CategoriaResponseDto;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriaControllerImpl implements ICategoriaController {

    private final CategoriaService categoriaService;

    @Override
    public ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> cadastrar(
            CategoriaRequestDto dto) {

        CategoriaResponseDto response = categoriaService.cadastrar(dto);

        return ResponseEntity.ok(DefaultApiResponse.success(new ApiResponseData<>(response)));
    }

    @Override
    public ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> buscarPorId(
            Long id) {

        CategoriaResponseDto response = categoriaService.buscarPorId(id);

        return ResponseEntity.ok(DefaultApiResponse.success(new ApiResponseData<>(response)));
    }

    @Override
    public ResponseEntity<DefaultApiResponse<List<CategoriaResponseDto>>> listarTodos() {

        List<CategoriaResponseDto> response = categoriaService.listarTodos();

        return ResponseEntity.ok(
                DefaultApiResponse.success(new ApiResponseData<>(response)));
    }

    @Override
    public ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> atualizar(Long id, CategoriaRequestDto dto) {

        CategoriaResponseDto response = categoriaService.atualizar(id, dto);

        return ResponseEntity.ok(DefaultApiResponse.success(new ApiResponseData<>(response)));
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> remover(Long id) {

        categoriaService.remover(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(new ApiResponseData<>("Categoria removida com sucesso.")));
    }
}