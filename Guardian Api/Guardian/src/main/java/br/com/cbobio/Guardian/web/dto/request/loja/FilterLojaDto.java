
package br.com.cbobio.Guardian.web.dto.request.loja;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterLojaDto {

    private String numeroLoja;

    private String nomeFantasia;

    private String razaoSocial;

    private String cnpj;

    private Boolean ativo;
}
