
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
public class EntregarProdutoRequest implements Serializable {

    @NotNull
    @Positive
    private Long retiradaId;

    @NotNull
    @Positive
    private Long produtoId;

    @NotNull
    @Positive
    private Long lojaId;

    @NotNull
    @Positive
    private Integer quantidade;
}