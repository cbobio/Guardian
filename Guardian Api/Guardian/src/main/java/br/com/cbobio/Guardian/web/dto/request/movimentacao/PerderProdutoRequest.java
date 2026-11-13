

package br.com.cbobio.Guardian.web.dto.request.movimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PerderProdutoRequest(

        @NotNull(message = "O ID do usuário é obrigatório.")
        @Positive(message = "O ID do usuário deve ser positivo.")
        Long usuarioId,

        @NotNull(message = "O ID da movimentação é obrigatório.")
        @Positive(message = "O ID da movimentação deve ser positivo.")
        Long movimentacaoId

) {

}