/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_categoria", uniqueConstraints = {@UniqueConstraint(name = "uk_categoria_nome", columnNames = "nome")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class TbCategoria extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Size(max = 250)
    @Column(name = "descricao", length = 250)
    private String descricao;

    @Column(name = "alto_risco", nullable = false)
    private Boolean altoRisco = false;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
