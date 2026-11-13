/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.repository.specification;

import com.atacadao.Guardian.model.entity.TbLoja;
import com.atacadao.Guardian.web.dto.request.loja.FilterLojaDto;
import org.springframework.data.jpa.domain.Specification;

public class LojaSpecification {

    private LojaSpecification(){}

    public static Specification<TbLoja> fromFiltro(FilterLojaDto filter){
        Specification<TbLoja> spec = Specification.unrestricted();

        if(filter == null){
            return spec;
        }

        if(filter.getNumeroLoja() != null && !filter.getNumeroLoja().isBlank()){
            spec = spec.and(numeroLojaLike(filter.getNumeroLoja()));
        }

        if(filter.getNomeFantasia() != null && !filter.getNomeFantasia().isBlank()){
            spec = spec.and(nomeFantasiaLike(filter.getNomeFantasia()));
        }

        if(filter.getRazaoSocial() != null && !filter.getRazaoSocial().isBlank()){
            spec = spec.and(razaoSocialLike(filter.getRazaoSocial()));
        }

        if(filter.getCnpj() != null && !filter.getCnpj().isBlank()){
            spec = spec.and(cnpjLike(filter.getCnpj()));
        }

        if (filter.getAtivo() != null) {
            spec = spec.and(ativo(filter.getAtivo()));
        }


        return spec;
    }

    public static Specification<TbLoja> numeroLojaLike(String numeroLoja){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("numeroLoja")), "%" + numeroLoja.toLowerCase() + "%");
    }

    public static Specification<TbLoja> nomeFantasiaLike(String nomeFantasia){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeFantasia")), "%" + nomeFantasia.toLowerCase() + "%");
    }

    public static Specification<TbLoja> razaoSocialLike(String razaoSocial){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("razaoSocial")), "%" + razaoSocial.toLowerCase() + "%");
    }

    public static Specification<TbLoja> cnpjLike(String cnpj){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("cnpj")), "%" + cnpj.toLowerCase() + "%");
    }

    public static Specification<TbLoja> ativo(Boolean ativo) {
        return (root, query, cb) -> cb.equal(root.get("ativo"), ativo);
    }
}
