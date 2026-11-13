/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.entity;

import com.atacadao.Guardian.model.entity.BaseEntity;
import com.atacadao.Guardian.model.entity.TbEstoque;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.entity.TbUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_loja", uniqueConstraints = { @UniqueConstraint(name = "uk_loja_numero", columnNames = "numero_loja"),
                @UniqueConstraint(name = "uk_loja_cnpj", columnNames = "cnpj")})
@ToString(callSuper = true, exclude = {"usuarios", "estoques", "movimentacoes"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TbLoja extends BaseEntity {

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "numero_loja", nullable = false, unique = true, length = 4)
    private String numeroLoja;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nome_fantasia", nullable = false, length = 150)
    private String nomeFantasia;

    @NotBlank
    @Size(max = 200)
    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @NotBlank
    @Size(min = 14, max = 14)
    @Column(name = "cnpj", nullable = false,unique = true, length = 14)
    private String cnpj;

    @Column(name = "logradouro", length = 200)
    private String logradouro;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Size(min = 2, max = 2)
    @Column(name = "uf", length = 2)
    private String uf;

    @Size(min = 8, max = 8)
    @Column(name = "cep", length = 8)
    private String cep;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Email
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Boolean controleProdutosAltoRisco = true;

    @OneToMany(mappedBy = "loja", fetch = FetchType.LAZY)
    private List<TbUsuario> usuarios;

    @OneToMany(mappedBy = "loja", fetch = FetchType.LAZY)
    private List<TbEstoque> estoques;

    @OneToMany(mappedBy = "loja", fetch = FetchType.LAZY)
    private List<TbMovimentacaoProduto> movimentacoes;
}
