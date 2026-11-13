/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.repository;

import com.atacadao.Guardian.model.entity.TbLoja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LojaRepository extends JpaRepository<TbLoja, Long>, JpaSpecificationExecutor<TbLoja> {

    boolean existsByNumeroLoja(String numeroLoja);

    boolean existsByNumeroLojaAndIdNot(String numeroLoja, Long id);

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);
}
