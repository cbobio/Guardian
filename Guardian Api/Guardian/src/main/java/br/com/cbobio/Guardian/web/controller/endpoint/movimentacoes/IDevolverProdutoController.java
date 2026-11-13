
package br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes;

import br.com.cbobio.Guardian.web.dto.request.movimentacao.DevolverProdutoRequest;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/movimentacoes")
public interface IDevolverProdutoController {

    @PostMapping("/devolver")
    ResponseEntity<DefaultApiResponse<String>> devolverProduto(
            @Valid @RequestBody DevolverProdutoRequest request
    );
}