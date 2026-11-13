/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.enums.enumState;

import lombok.Getter;

@Getter
public enum MachineEstados {
    ESTOQUE("Rascunho"),
    RETIRADO("Retirado"),
    ENTREGUE("Entregue"),
    DEVOLVIDO("Devolvido"),
    AVARIADO("Avariado"),
    PERDIDO("Perdido"),
    FINALIZADO("Finalizado");

    private final String descricao;

    MachineEstados(String descricao){
        this.descricao = descricao;
    }
}
