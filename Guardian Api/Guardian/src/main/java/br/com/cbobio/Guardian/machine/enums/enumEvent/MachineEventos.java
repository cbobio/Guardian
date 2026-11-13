
package br.com.cbobio.Guardian.machine.enums.enumEvent;

import lombok.Getter;

@Getter
public enum MachineEventos {
    RETORNAR_ESTOQUE("Receber Produto"),
    RETIRAR("Retirar"),
    ENTREGAR("Entregar"),
    DEVOLVER("Devolver"),
    AVARIAR("Avariar"),
    PERDER("Perder"),
    FINALIZAR("Finalizar");

    private final String descricao;

    MachineEventos(String descricao){
        this.descricao = descricao;
    }
}
