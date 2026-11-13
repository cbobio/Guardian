
package br.com.cbobio.Guardian.web.controller.endpoint;


import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IFinalizarProdutoController {

    @PostMapping("/finalizar/{usuarioId}/{movimentacaoId}")
    ResponseEntity<DefaultApiResponse<String>> finalizarProduto(
            @PathVariable Long usuarioId,
            @PathVariable Long movimentacaoId,
            @RequestParam(required = false) String observacao
    );
}