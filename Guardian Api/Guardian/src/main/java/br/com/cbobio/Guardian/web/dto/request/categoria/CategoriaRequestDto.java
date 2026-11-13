

package br.com.cbobio.Guardian.web.dto.request.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoriaRequestDto implements Serializable {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @Size(max = 250)
    private String descricao;

    private Boolean altoRisco = false;

    private Boolean ativo = true;

}
