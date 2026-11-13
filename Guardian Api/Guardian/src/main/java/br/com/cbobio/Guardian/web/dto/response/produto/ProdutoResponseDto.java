
package br.com.cbobio.Guardian.web.dto.response.produto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProdutoResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private String codigoBarras;

    private String nome;

    private String descricao;

    private String marca;

    private BigDecimal custo;

    private Boolean altoRisco;

    private Boolean ativo;

    private Long categoriaId;
}