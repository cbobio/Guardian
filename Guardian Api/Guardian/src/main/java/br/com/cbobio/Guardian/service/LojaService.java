/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.repository.LojaRepository;
import com.atacadao.Guardian.model.repository.specification.LojaSpecification;
import com.atacadao.Guardian.web.dto.mapper.LojaMapper;
import com.atacadao.Guardian.web.dto.request.loja.FilterLojaDto;
import com.atacadao.Guardian.web.dto.request.loja.LojaRequestDto;
import com.atacadao.Guardian.web.dto.response.loja.LojaResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LojaService {

    private final LojaRepository lojaRepository;
    private final LojaMapper lojaMapper;

    public LojaResponseDto cadastrar(LojaRequestDto dto) {

        if (lojaRepository.existsByNumeroLoja(dto.getNumeroLoja())) {
            throw new IllegalArgumentException(
                    "Já existe uma loja cadastrada com o número informado."
            );
        }

        if (lojaRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException(
                    "Já existe uma loja cadastrada com o CNPJ informado."
            );
        }

        TbLoja loja = lojaMapper.toEntity(dto);

        TbLoja lojaSalva = lojaRepository.save(loja);

        return lojaMapper.toResponseDto(lojaSalva);
    }

    public LojaResponseDto buscarPorId(Long id) {

        TbLoja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Loja não encontrada."
                ));

        return lojaMapper.toResponseDto(loja);
    }

    @Transactional(readOnly = true)
    public Page<LojaResponseDto> listarTodos(
            FilterLojaDto filtro,
            Pageable pageable) {

        Specification<TbLoja> specification =
                LojaSpecification.fromFiltro(filtro);

        return lojaRepository.findAll(specification, pageable)
                .map(lojaMapper::toResponseDto);
    }

    public LojaResponseDto atualizar(
            Long id,
            LojaRequestDto dto) {

        TbLoja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Loja não encontrada."
                ));

        if (lojaRepository.existsByNumeroLojaAndIdNot(
                dto.getNumeroLoja(),
                id)) {

            throw new IllegalArgumentException(
                    "Já existe outra loja cadastrada com o número informado."
            );
        }

        if (lojaRepository.existsByCnpjAndIdNot(
                dto.getCnpj(),
                id)) {

            throw new IllegalArgumentException(
                    "Já existe outra loja cadastrada com o CNPJ informado."
            );
        }

        loja.setNumeroLoja(dto.getNumeroLoja());
        loja.setNomeFantasia(dto.getNomeFantasia());
        loja.setRazaoSocial(dto.getRazaoSocial());
        loja.setCnpj(dto.getCnpj());
        loja.setLogradouro(dto.getLogradouro());
        loja.setNumero(dto.getNumero());
        loja.setBairro(dto.getBairro());
        loja.setCidade(dto.getCidade());
        loja.setUf(dto.getUf());
        loja.setCep(dto.getCep());
        loja.setTelefone(dto.getTelefone());
        loja.setEmail(dto.getEmail());
        loja.setAtivo(dto.getAtivo());
        loja.setControleProdutosAltoRisco(
                dto.getControleProdutosAltoRisco()
        );

        TbLoja lojaAtualizada =
                lojaRepository.save(loja);

        return lojaMapper.toResponseDto(lojaAtualizada);
    }

    public void remover(Long id) {

        TbLoja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Loja não encontrada."
                ));

        lojaRepository.delete(loja);
    }
}