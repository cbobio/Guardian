
package br.com.cbobio.Guardian.web.dto.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioRequestDto implements Serializable {

    @NotBlank
    @Size(max = 20)
    private String matricula;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String senha;

    @NotNull
    private Boolean ativo = true;

    @NotNull
    private Boolean bloqueado = false;

    @NotNull
    private Long lojaId;

    @NotNull
    private Long roleId;
}