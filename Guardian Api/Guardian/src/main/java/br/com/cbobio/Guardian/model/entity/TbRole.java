/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
*/

package br.com.cbobio.Guardian.model.entity;

import com.atacadao.Guardian.enums.Roles;
import com.atacadao.Guardian.model.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;

@Entity
@Table( name = "tb_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TbRole extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "roles_usuario", nullable = false, unique = true, length = 50)
    private Roles role;

    @NotBlank
    @Column(name = "descricao", nullable = false, length = 250)
    private String descricao;

}
