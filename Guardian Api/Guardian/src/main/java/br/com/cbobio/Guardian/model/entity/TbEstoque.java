/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.entity;

import com.atacadao.Guardian.model.entity.BaseEntity;
import com.atacadao.Guardian.model.entity.TbProduto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "tb_estoque", uniqueConstraints = {@UniqueConstraint(name = "uk_estoque_loja_produto", columnNames = {
        "loja_id", "produto_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"loja", "produto"})
public class TbEstoque extends BaseEntity {

    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade = 0;

    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private TbLoja loja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private TbProduto produto;
}
