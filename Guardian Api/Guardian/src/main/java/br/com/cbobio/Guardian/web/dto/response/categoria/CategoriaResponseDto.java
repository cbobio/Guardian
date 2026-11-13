

package br.com.cbobio.Guardian.web.dto.response.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoriaResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private String nome;

    private String descricao;

    private Boolean altoRisco;

    private Boolean ativo;
}
