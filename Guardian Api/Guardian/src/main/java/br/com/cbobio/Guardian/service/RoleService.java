/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.model.entity.TbRole;
import com.atacadao.Guardian.model.repository.RoleRepository;
import com.atacadao.Guardian.web.dto.mapper.RoleMapper;
import com.atacadao.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import com.atacadao.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponseDto cadastrar(RoleRequestDto dto) {

        if (roleRepository.existsByRole(dto.getRole())) {
            throw new IllegalArgumentException(
                    "Já existe uma role cadastrada com o perfil informado."
            );
        }

        TbRole role = roleMapper.toEntity(dto);

        TbRole roleSalva = roleRepository.save(role);

        return roleMapper.toResponseDto(roleSalva);
    }

    public RoleResponseDto buscarPorId(Long id) {

        TbRole role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role não encontrada."
                        )
                );

        return roleMapper.toResponseDto(role);
    }

    public List<RoleResponseDto> listarTodos() {

        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponseDto)
                .toList();
    }

    public RoleResponseDto atualizar(
            Long id,
            RoleRequestDto dto
    ) {

        TbRole role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role não encontrada."
                        )
                );

        if (roleRepository.existsByRoleAndIdNot(
                dto.getRole(),
                id
        )) {

            throw new IllegalArgumentException(
                    "Já existe outra role cadastrada com o perfil informado."
            );
        }

        role.setRole(dto.getRole());
        role.setDescricao(dto.getDescricao());

        TbRole roleAtualizada =
                roleRepository.save(role);

        return roleMapper.toResponseDto(roleAtualizada);
    }

    public void remover(Long id) {

        TbRole role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role não encontrada."
                        )
                );

        roleRepository.delete(role);
    }
}