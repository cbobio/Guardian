 /*
  * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
  */

 package br.com.cbobio.Guardian.web.controller;

 import com.atacadao.Guardian.service.ProdutoService;
 import com.atacadao.Guardian.web.controller.endpoint.IProdutoController;
 import com.atacadao.Guardian.web.dto.request.produto.FilterProdutoDto;
 import com.atacadao.Guardian.web.dto.request.produto.ProdutoRequestDto;
 import com.atacadao.Guardian.web.dto.response.produto.ProdutoResponseDto;
 import com.atacadao.Guardian.web.interceptor.ApiResponseData;
 import com.atacadao.Guardian.web.interceptor.DefaultApiResponse;
 import lombok.RequiredArgsConstructor;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.RestController;

 import java.util.List;

 @RestController
 @RequiredArgsConstructor
 public class ProdutoControllerImpl implements IProdutoController {

     private final ProdutoService produtoService;

     @Override
     public ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> cadastrar(
             ProdutoRequestDto dto) {

         ProdutoResponseDto response = produtoService.cadastrar(dto);

         return ResponseEntity.ok(
                 DefaultApiResponse.success(
                         new ApiResponseData<>(response)
                 )
         );
     }

     @Override
     public ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> buscarPorId(
             Long id) {

         ProdutoResponseDto response = produtoService.buscarPorId(id);

         return ResponseEntity.ok(
                 DefaultApiResponse.success(
                         new ApiResponseData<>(response)
                 )
         );
     }


     @Override
     public ResponseEntity<
             DefaultApiResponse<Page<ProdutoResponseDto>>> listarTodos(
             FilterProdutoDto filtro,
             Pageable pageable) {

         Page<ProdutoResponseDto> response =
                 produtoService.listarTodos(filtro, pageable);

         return ResponseEntity.ok(
                 DefaultApiResponse.success(
                         new ApiResponseData<>(response)
                 )
         );
     }

     @Override
     public ResponseEntity<DefaultApiResponse<ProdutoResponseDto>> atualizar(
             Long id,
             ProdutoRequestDto dto) {

         ProdutoResponseDto response = produtoService.atualizar(id, dto);

         return ResponseEntity.ok(
                 DefaultApiResponse.success(
                         new ApiResponseData<>(response)
                 )
         );
     }

     @Override
     public ResponseEntity<DefaultApiResponse<String>> remover(Long id) {

         produtoService.remover(id);

         return ResponseEntity.ok(
                 DefaultApiResponse.success(
                         new ApiResponseData<>("Produto removido com sucesso.")
                 )
         );
     }
 }