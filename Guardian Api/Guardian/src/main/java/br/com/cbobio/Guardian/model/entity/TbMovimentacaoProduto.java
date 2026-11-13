/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.model.entity;


import br.com.cbobio.Guardian.enums.MotivoMovimentacao;
import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tb_movimentacao_produto",
        indexes = {
                @Index(name = "idx_mov_produto", columnList = "produto_id"),
                @Index(name = "idx_mov_loja", columnList = "loja_id"),
                @Index(name = "idx_mov_usuario", columnList = "usuario_id"),
                @Index(name = "idx_mov_estado", columnList = "estado"),
                @Index(name = "idx_mov_data", columnList = "data_movimentacao"),
                @Index(name = "idx_mov_loja_estado", columnList = "loja_id,estado"),
                @Index(name = "idx_mov_estado_anterior", columnList = "estado_anterior"),
                @Index(name = "idx_movimentacao_retirada", columnList = "retirada_id"),
                @Index(name = "idx_movimentacao_origem", columnList = "movimentacao_origem_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(
        callSuper = true,
        exclude = {"retirada", "movimentacaoOrigem"}
)
@ToString(
        callSuper = true,
        exclude = {
                "produto",
                "loja",
                "usuario",
                "retirada",
                "movimentacaoOrigem"
        }
)
public class TbMovimentacaoProduto extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "evento", nullable = false, length = 30)
    private MachineEventos evento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private MachineEstados estado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false, length = 30)
    private MotivoMovimentacao motivo;

    @NotNull
    @Positive
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name = "data_movimentacao", nullable = false, updatable = false)
    private LocalDateTime dataMovimentacao;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 20)
    private MachineEstados estadoAnterior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private TbProduto produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private TbLoja loja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private TbUsuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retirada_id")
    private TbMovimentacaoProduto retirada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movimentacao_origem_id")
    private TbMovimentacaoProduto movimentacaoOrigem;

    @PrePersist
    private void prePersist() {
        dataMovimentacao = LocalDateTime.now();
    }
}