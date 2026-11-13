/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.model.entity.TbCategoria;
import com.atacadao.Guardian.model.entity.TbProduto;
import com.atacadao.Guardian.model.repository.CategoriaRepository;
import com.atacadao.Guardian.model.repository.ProdutoRepository;
import com.atacadao.Guardian.model.repository.specification.ProdutoSpecification;
import com.atacadao.Guardian.web.dto.mapper.ProdutoMapper;
import com.atacadao.Guardian.web.dto.request.produto.FilterProdutoDto;
import com.atacadao.Guardian.web.dto.request.produto.ProdutoRequestDto;
import com.atacadao.Guardian.web.dto.response.produto.ProdutoResponseDto;
import jakarta.persistence.EntityNotFoundException;
import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.model.repository.EstoqueRepository;
import com.atacadao.Guardian.model.repository.LojaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponseDto cadastrar(ProdutoRequestDto dto) {

        if (dto.getLojaId() == null
                || dto.getQuantidadeInicial() == null
                || dto.getQuantidadeMinima() == null) {

            throw new IllegalArgumentException(
                    "Loja, quantidade inicial e quantidade mínima são obrigatórias."
            );
        }

        if (dto.getQuantidadeInicial() < 0
                || dto.getQuantidadeMinima() < 0) {

            throw new IllegalArgumentException(
                    "As quantidades não podem ser negativas."
            );
        }

        if (produtoRepository.existsByCodigoBarras(dto.getCodigoBarras())) {
            throw new IllegalArgumentException(
                    "Já existe um produto cadastrado com o código de barras informado."
            );
        }

        TbCategoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada."
                ));

        TbLoja loja = lojaRepository.findById(dto.getLojaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Loja não encontrada."
                ));

        // 1. Cadastrar o produto
        TbProduto produto = produtoMapper.toEntity(dto);
        produto.setCategoria(categoria);

        TbProduto produtoSalvo = produtoRepository.save(produto);

        // 2. Criar o estoque inicial associado ao produto e à loja
        TbEstoque estoque = new TbEstoque();
        estoque.setProduto(produtoSalvo);
        estoque.setLoja(loja);
        estoque.setQuantidade(dto.getQuantidadeInicial());
        estoque.setQuantidadeMinima(dto.getQuantidadeMinima());

        estoqueRepository.save(estoque);

        // 3. Retornar a resposta do produto cadastrado
        return produtoMapper.toResponseDto(produtoSalvo);
    }

    public ProdutoResponseDto buscarPorId(Long id) {

        TbProduto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Produto não encontrado."
                ));

        return produtoMapper.toResponseDto(produto);
    }


    public Page<ProdutoResponseDto> listarTodos(FilterProdutoDto filtro, Pageable pageable) {
        Specification<TbProduto> specification = ProdutoSpecification.fromFiltro(filtro);

        return produtoRepository.findAll(specification, pageable)
                .map(produtoMapper::toResponseDto);
    }

    public ProdutoResponseDto atualizar(
            Long id,
            ProdutoRequestDto dto) {

        TbProduto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Produto não encontrado."
                ));

        if (!produto.getCodigoBarras().equals(dto.getCodigoBarras())
                && produtoRepository.existsByCodigoBarras(dto.getCodigoBarras())) {

            throw new IllegalArgumentException(
                    "Já existe outro produto cadastrado com o código de barras informado."
            );
        }

        TbCategoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada."
                ));

        produto.setCodigoBarras(dto.getCodigoBarras());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setMarca(dto.getMarca());
        produto.setCusto(dto.getCusto());
        produto.setAltoRisco(dto.getAltoRisco());
        produto.setAtivo(dto.getAtivo());
        produto.setCategoria(categoria);

        TbProduto produtoAtualizado = produtoRepository.save(produto);

        return produtoMapper.toResponseDto(produtoAtualizado);
    }

    public void remover(Long id) {

        TbProduto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Produto não encontrado."
                ));

        produtoRepository.delete(produto);
    }
}