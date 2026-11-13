/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.model.entity.TbCategoria;
import com.atacadao.Guardian.model.repository.CategoriaRepository;
import com.atacadao.Guardian.web.dto.mapper.CategoriaMapper;
import com.atacadao.Guardian.web.dto.request.categoria.CategoriaRequestDto;
import com.atacadao.Guardian.web.dto.response.categoria.CategoriaResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaResponseDto cadastrar(CategoriaRequestDto dto) {

        if (categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria cadastrada com o nome informado."
            );
        }

        TbCategoria categoria = categoriaMapper.toEntity(dto);

        TbCategoria categoriaSalva =
                categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDto(categoriaSalva);
    }

    public CategoriaResponseDto buscarPorId(Long id) {

        TbCategoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada."
                ));

        return categoriaMapper.toResponseDto(categoria);
    }

    public List<CategoriaResponseDto> listarTodos() {

        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponseDto)
                .toList();
    }

    public CategoriaResponseDto atualizar(
            Long id,
            CategoriaRequestDto dto) {

        TbCategoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada."
                ));

        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(
                dto.getNome(),
                id)) {

            throw new IllegalArgumentException(
                    "Já existe outra categoria cadastrada com o nome informado."
            );
        }

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setAltoRisco(dto.getAltoRisco());
        categoria.setAtivo(dto.getAtivo());

        TbCategoria categoriaAtualizada =
                categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDto(categoriaAtualizada);
    }

    public void remover(Long id) {

        TbCategoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada."
                ));

        categoriaRepository.delete(categoria);
    }
}