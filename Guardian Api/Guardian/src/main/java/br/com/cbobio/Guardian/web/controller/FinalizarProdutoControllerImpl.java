
package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.MovimentacaoProdutoService;
import br.com.cbobio.Guardian.web.controller.endpoint.IFinalizarProdutoController;
import br.com.cbobio.Guardian.web.interceptor.ApiResponseData;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FinalizarProdutoControllerImpl
        implements IFinalizarProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> finalizarProduto(
            Long usuarioId,
            Long movimentacaoId,
            String observacao
    ) {
        String mensagem = movimentacaoProdutoService.finalizarProduto(
                usuarioId,
                movimentacaoId,
                observacao
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(mensagem)
                )
        );
    }
}