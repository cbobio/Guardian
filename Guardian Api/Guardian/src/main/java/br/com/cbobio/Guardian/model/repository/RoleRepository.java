/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.repository;

import com.atacadao.Guardian.enums.Roles;
import com.atacadao.Guardian.model.entity.TbRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<TbRole, Long> {
    boolean existsByRole(Roles role);

    boolean existsByRoleAndIdNot(Roles role, Long id);
}
