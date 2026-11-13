
package br.com.cbobio.Guardian.web.dto.response.movimentacao;


import br.com.cbobio.Guardian.enums.MotivoMovimentacao;
import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MovimentacaoProdutoResponseDto implements Serializable {

    private Long id;

    private MachineEventos evento;

    private MachineEstados estado;

    private MotivoMovimentacao motivo;

    private Integer quantidade;

    private LocalDateTime dataMovimentacao;

    private String observacao;

    private MachineEstados estadoAnterior;

    private Long produtoId;

    private Long lojaId;

    private Long usuarioId;
}