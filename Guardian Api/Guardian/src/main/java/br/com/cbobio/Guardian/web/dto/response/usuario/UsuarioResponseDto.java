
package br.com.cbobio.Guardian.web.dto.response.usuario;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private String matricula;

    private String nome;

    private String email;

    private Boolean ativo;

    private Boolean bloqueado;

    private LocalDateTime ultimoLogin;

    private Long lojaId;

    private Long roleId;
}