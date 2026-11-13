

package br.com.cbobio.Guardian.web.dto.response.estoque;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EstoqueResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private Long produtoId;

    private Long lojaId;

    private Integer quantidade;

    private Integer quantidadeMinima;

}