/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.entity.TbProduto;
import com.atacadao.Guardian.model.repository.EstoqueRepository;
import com.atacadao.Guardian.model.repository.LojaRepository;
import com.atacadao.Guardian.model.repository.ProdutoRepository;
import com.atacadao.Guardian.web.dto.mapper.EstoqueMapper;
import com.atacadao.Guardian.web.dto.request.estoque.EstoqueRequestDto;
import com.atacadao.Guardian.web.dto.response.estoque.EstoqueResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueMapper estoqueMapper;

    public EstoqueResponseDto cadastrar(EstoqueRequestDto dto) {

        if (estoqueRepository.findByLojaIdAndProdutoId(dto.getLojaId(), dto.getProdutoId())
                .isPresent()) {
            throw new IllegalArgumentException("Já existe um estoque cadastrado para o produto e loja informados.");
        }

        TbLoja loja = lojaRepository.findById(dto.getLojaId()).orElseThrow(()
                -> new EntityNotFoundException("Loja não encontrada."));

        TbProduto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        TbEstoque estoque = estoqueMapper.toEntity(dto);

        estoque.setLoja(loja);
        estoque.setProduto(produto);

        TbEstoque estoqueSalvo = estoqueRepository.save(estoque);

        return estoqueMapper.toResponseDto(estoqueSalvo);
    }

    public EstoqueResponseDto buscarPorId(Long id) {

        TbEstoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado."));

        return estoqueMapper.toResponseDto(estoque);
    }

    public EstoqueResponseDto buscarPorLojaEProduto(Long lojaId, Long produtoId) {

        TbEstoque estoque = estoqueRepository
                .findByLojaIdAndProdutoId(lojaId, produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado para a loja e produto informados."));

        return estoqueMapper.toResponseDto(estoque);
    }

    public List<EstoqueResponseDto> listarTodos() {

        return estoqueRepository.findAll()
                .stream()
                .map(estoqueMapper::toResponseDto)
                .toList();
    }

    public EstoqueResponseDto atualizar(Long id, EstoqueRequestDto dto) {

        TbEstoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado."));

        estoqueRepository.findByLojaIdAndProdutoId(dto.getLojaId(), dto.getProdutoId())
                .filter(outroEstoque -> !outroEstoque.getId().equals(id))
                .ifPresent(outroEstoque -> {
                    throw new IllegalArgumentException("Já existe outro estoque cadastrado para o produto e loja informados.");
                });

        TbLoja loja = lojaRepository.findById(dto.getLojaId())
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada."));

        TbProduto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        estoque.setLoja(loja);
        estoque.setProduto(produto);
        estoque.setQuantidade(dto.getQuantidade());
        estoque.setQuantidadeMinima(dto.getQuantidadeMinima());

        TbEstoque estoqueAtualizado =
                estoqueRepository.save(estoque);

        return estoqueMapper.toResponseDto(estoqueAtualizado);
    }

    public void remover(Long id) {

        TbEstoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado."));

        estoqueRepository.delete(estoque);
    }
}