
package br.com.cbobio.Guardian.web.dto.request.estoque;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EstoqueRequestDto implements Serializable {

    @NotNull
    private Long produtoId;

    @NotNull
    private Long lojaId;

    @NotNull
    @PositiveOrZero
    private Integer quantidade;

    @NotNull
    @PositiveOrZero
    private Integer quantidadeMinima;


}
