/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.web.controller;

import com.atacadao.Guardian.service.RoleService;
import com.atacadao.Guardian.web.controller.endpoint.IRoleController;
import com.atacadao.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import com.atacadao.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import com.atacadao.Guardian.web.interceptor.ApiResponseData;
import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl
        implements IRoleController {

    private final RoleService roleService;

    @Override
    public ResponseEntity<DefaultApiResponse<RoleResponseDto>> cadastrar(
            RoleRequestDto dto
    ) {

        RoleResponseDto response =
                roleService.cadastrar(dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<RoleResponseDto>> buscarPorId(
            Long id
    ) {

        RoleResponseDto response =
                roleService.buscarPorId(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<List<RoleResponseDto>>> listarTodos() {

        List<RoleResponseDto> response =
                roleService.listarTodos();

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<RoleResponseDto>> atualizar(
            Long id,
            RoleRequestDto dto
    ) {

        RoleResponseDto response =
                roleService.atualizar(id, dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> remover(
            Long id
    ) {

        roleService.remover(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Role removida com sucesso."
                        )
                )
        );
    }
}