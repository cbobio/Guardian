
package br.com.cbobio.Guardian.web.dto.response.loja;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LojaResponseDto implements Serializable {

    private Long id;

    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    private String numeroLoja;

    private String nomeFantasia;

    private String razaoSocial;

    private String cnpj;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cidade;

    private String uf;

    private String cep;

    private String telefone;

    private String email;

    private Boolean ativo;

    private Boolean controleProdutosAltoRisco;
}