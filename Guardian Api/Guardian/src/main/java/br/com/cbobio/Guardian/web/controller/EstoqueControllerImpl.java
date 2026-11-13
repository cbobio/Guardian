/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.EstoqueService;
import com.atacadao.Guardian.web.controller.endpoint.IEstoqueController;
import com.atacadao.Guardian.web.dto.request.estoque.EstoqueRequestDto;
import com.atacadao.Guardian.web.dto.response.estoque.EstoqueResponseDto;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EstoqueControllerImpl implements IEstoqueController {

    private final EstoqueService estoqueService;

    @Override
    public ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> cadastrar(EstoqueRequestDto dto) {
        EstoqueResponseDto response = estoqueService.cadastrar(dto);
        return ResponseEntity.ok(DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> buscarPorId(Long id) {
        EstoqueResponseDto response = estoqueService.buscarPorId(id);
        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> buscarPorLojaEProduto(Long lojaId, Long produtoId) {
        EstoqueResponseDto response = estoqueService.buscarPorLojaEProduto(lojaId, produtoId);
        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<List<EstoqueResponseDto>>> listarTodos() {

        List<EstoqueResponseDto> response = estoqueService.listarTodos();
        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> atualizar(Long id, EstoqueRequestDto dto) {

        EstoqueResponseDto response = estoqueService.atualizar(id, dto);
        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> remover(Long id) {
        estoqueService.remover(id);
        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Estoque removido com sucesso."
                        )
                )
        );
    }
}