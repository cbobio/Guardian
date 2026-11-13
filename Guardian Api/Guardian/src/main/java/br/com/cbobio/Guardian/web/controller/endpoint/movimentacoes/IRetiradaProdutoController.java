
package br.com.cbobio.Guardian.web.controller.endpoint.movimentacoes;

import br.com.cbobio.Guardian.web.dto.request.movimentacao.EntregarProdutoRequest;
import br.com.cbobio.Guardian.web.dto.request.movimentacao.RetiradaProdutoRequest;
import br.com.cbobio.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/movimentacoes")
public interface IRetiradaProdutoController {

    @PostMapping("/retirar")
    ResponseEntity<DefaultApiResponse<String>> retirarProduto(
            @Valid @RequestBody RetiradaProdutoRequest request
    );

    @PostMapping("/entregar")
    ResponseEntity<DefaultApiResponse<String>> entregarProduto(
            @Valid @RequestBody EntregarProdutoRequest request
    );

    @GetMapping
    ResponseEntity<
            DefaultApiResponse<Page<MovimentacaoProdutoResponseDto>>
            > listarTodos(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "dataMovimentacao",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    );
}