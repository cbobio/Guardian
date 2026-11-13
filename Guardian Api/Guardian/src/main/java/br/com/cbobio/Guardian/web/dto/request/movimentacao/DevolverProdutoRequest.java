
package br.com.cbobio.Guardian.web.dto.request.movimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolverProdutoRequest {

    @NotNull(message = "O ID do usuário é obrigatório.")
    @Positive(message = "O ID do usuário deve ser maior que zero.")
    private Long usuarioId;

    @NotNull(message = "O ID da movimentação é obrigatório.")
    @Positive(message = "O ID da movimentação deve ser maior que zero.")
    private Long movimentacaoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres.")
    private String observacao;
}