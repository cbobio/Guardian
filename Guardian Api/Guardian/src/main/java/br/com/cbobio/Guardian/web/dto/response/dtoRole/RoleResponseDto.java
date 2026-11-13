
package br.com.cbobio.Guardian.web.dto.response.dtoRole;


import br.com.cbobio.Guardian.enums.Roles;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private Roles role;

    private String descricao;
}