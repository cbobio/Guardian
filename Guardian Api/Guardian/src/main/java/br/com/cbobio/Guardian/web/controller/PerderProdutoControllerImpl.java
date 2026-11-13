
package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.MovimentacaoProdutoService;
import br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes.IPerderProdutoController;
import br.com.cbobio.Guardian.web.dto.request.movimentacao.PerderProdutoRequest;
import br.com.cbobio.Guardian.web.interceptor.ApiResponseData;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PerderProdutoControllerImpl
        implements IPerderProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> perderProduto(
            PerderProdutoRequest request
    ) {

        movimentacaoProdutoService.perderProduto(
                request.usuarioId(),
                request.movimentacaoId()
        );

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Produto perdido registrado com sucesso."
                        )
                )
        );
    }
}