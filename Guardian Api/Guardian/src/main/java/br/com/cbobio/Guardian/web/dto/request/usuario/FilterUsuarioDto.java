
package br.com.cbobio.Guardian.web.dto.request.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterUsuarioDto {

    private String matricula;

    private String nome;

    private String email;

    private Boolean ativo ;
}
