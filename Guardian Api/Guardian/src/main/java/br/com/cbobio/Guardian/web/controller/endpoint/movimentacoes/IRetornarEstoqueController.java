
package br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes;



import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/movimentacoes")
public interface IRetornarEstoqueController {

    @PostMapping("/{movimentacaoId}/retornar-estoque")
    ResponseEntity<DefaultApiResponse<String>> retornarProdutoAoEstoque(
            @PathVariable Long movimentacaoId,
            @RequestParam Long usuarioId
    );
}