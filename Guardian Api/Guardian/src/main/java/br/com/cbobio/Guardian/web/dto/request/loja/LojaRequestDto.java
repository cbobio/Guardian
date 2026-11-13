
package br.com.cbobio.Guardian.web.dto.request.loja;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LojaRequestDto implements Serializable {

    @NotBlank
    @Size(min = 4, max = 4)
    private String numeroLoja;

    @NotBlank
    @Size(max = 150)
    private String nomeFantasia;

    @NotBlank
    @Size(max = 200)
    private String razaoSocial;

    @NotBlank
    @Size(min = 14, max = 14)
    private String cnpj;

    @Size(max = 200)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Size(min = 2, max = 2)
    private String uf;

    @Size(min = 8, max = 8)
    private String cep;

    @Size(max = 20)
    private String telefone;

    @Email
    @Size(max = 150)
    private String email;

    private Boolean ativo = true;

    private Boolean controleProdutosAltoRisco = true;
}