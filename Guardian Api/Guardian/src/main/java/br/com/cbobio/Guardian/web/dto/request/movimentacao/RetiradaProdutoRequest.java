
package br.com.cbobio.Guardian.web.dto.request.movimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetiradaProdutoRequest implements Serializable {

    @NotNull
    private Long produtoId;

    @NotNull
    private Long lojaId;

    @NotNull
    @Positive
    private Integer quantidade;
}