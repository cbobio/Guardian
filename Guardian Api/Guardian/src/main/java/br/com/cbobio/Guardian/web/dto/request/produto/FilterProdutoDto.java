
package br.com.cbobio.Guardian.web.dto.request.produto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterProdutoDto {

    private String nome;

    private String codigoBarras;

    private String descricao;

    private String marca;
}
