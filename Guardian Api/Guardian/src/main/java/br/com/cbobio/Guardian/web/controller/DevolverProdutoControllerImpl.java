

package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.MovimentacaoProdutoService;
import br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes.IDevolverProdutoController;
import br.com.cbobio.Guardian.web.dto.request.movimentacao.DevolverProdutoRequest;
import br.com.cbobio.Guardian.web.interceptor.ApiResponseData;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DevolverProdutoControllerImpl implements IDevolverProdutoController {

    private final MovimentacaoProdutoService movimentacaoProdutoService;

    @Override
    public ResponseEntity<DefaultApiResponse<String>> devolverProduto(
            DevolverProdutoRequest request
    ) {
        movimentacaoProdutoService.devolverProduto(
                request.getUsuarioId(),
                request.getMovimentacaoId(),
                request.getQuantidade(),
                request.getObservacao()
        );

        ApiResponseData<String> data = new ApiResponseData<>();
        data.setResource("Devolução de produto registrada com sucesso.");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(DefaultApiResponse.success(data));
    }
}