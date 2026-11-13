

package br.com.cbobio.Guardian.web.dto.request.movimentacao;

import br.com.cbobio.Guardian.enums.MotivoMovimentacao;
import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovimentacaoProdutoRequestDto implements Serializable {

    @NotNull
    private MachineEventos evento;

    @NotNull
    private MachineEstados estado;

    @NotNull
    private MotivoMovimentacao motivo;

    @NotNull
    @Positive
    private Integer quantidade;

    @Size(max = 500)
    private String observacao;

    private MachineEstados estadoAnterior;

    @NotNull
    private Long produtoId;

    @NotNull
    private Long lojaId;

    @NotNull
    private Long usuarioId;
}