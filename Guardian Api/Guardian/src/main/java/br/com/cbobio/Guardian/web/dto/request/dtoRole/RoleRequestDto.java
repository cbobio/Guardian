
package br.com.cbobio.Guardian.web.dto.request.dtoRole;

import br.com.cbobio.Guardian.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleRequestDto implements Serializable {

    @NotNull
    private Roles role;

    @NotBlank
    @Size(max = 250)
    private String descricao;
}