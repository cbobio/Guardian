
package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.MovimentacaoProdutoService;
import br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes.IRetornarEstoqueController;
import br.com.cbobio.Guardian.web.interceptor.ApiResponseData;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movimentacoes")
public class RetornarEstoqueControllerImpl
        implements IRetornarEstoqueController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    @PostMapping("/{movimentacaoId}/retornar-estoque")
    public ResponseEntity<DefaultApiResponse<String>> retornarProdutoAoEstoque(
            @PathVariable("movimentacaoId") Long movimentacaoId,
            @RequestParam("usuarioId") Long usuarioId
    ) {
        movimentacaoProdutoService.retornarProdutoAoEstoque(
                usuarioId,
                movimentacaoId
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Produto retornado ao estoque com sucesso."
                        )
                )
        );
    }
}