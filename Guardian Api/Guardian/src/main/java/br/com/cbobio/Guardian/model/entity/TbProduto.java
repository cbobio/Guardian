/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_produto", uniqueConstraints = {@UniqueConstraint(name = "uk_produto_codigo_barras", columnNames = "codigo_barras")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = "categoria")
public class TbProduto extends BaseEntity {

    @NotBlank
    @Size(min = 8, max = 14)
    @Column(name = "codigo_barras", nullable = false, length = 14)
    private String codigoBarras;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Size(max = 250)
    @Column(name = "descricao", length = 250)
    private String descricao;

    @NotBlank
    @Size(max = 100)
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @DecimalMin(value = "0.0")
    @Column(name = "custo",nullable = false, precision = 15, scale = 2)
    private BigDecimal custo;

    @NotNull
    @Column(name = "alto_risco", nullable = false)
    private Boolean altoRisco = false;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private TbCategoria categoria;
}
