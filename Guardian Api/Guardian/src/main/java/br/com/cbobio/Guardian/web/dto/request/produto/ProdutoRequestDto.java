
package br.com.cbobio.Guardian.web.dto.request.produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProdutoRequestDto implements Serializable {

    @NotBlank
    @Size(min = 8, max = 14)
    private String codigoBarras;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @Size(max = 250)
    private String descricao;

    @NotBlank
    @Size(max = 100)
    private String marca;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal custo;

    @NotNull
    private Boolean altoRisco = false;

    @NotNull
    private Boolean ativo = true;

    @NotNull
    private Long categoriaId;

    // Dados do estoque inicial
    @Positive
    private Long lojaId;

    @PositiveOrZero
    private Integer quantidadeInicial;

    @PositiveOrZero
    private Integer quantidadeMinima;
}