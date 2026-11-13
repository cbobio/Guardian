
package br.com.cbobio.Guardian.web.controller.endpoint;

import br.com.cbobio.Guardian.web.dto.request.estoque.EstoqueRequestDto;
import br.com.cbobio.Guardian.web.dto.response.estoque.EstoqueResponseDto;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/estoques")
public interface IEstoqueController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> cadastrar(@Valid @RequestBody EstoqueRequestDto dto);

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> buscarPorId(@PathVariable Long id);

    @GetMapping("/loja/{lojaId}/produto/{produtoId}")
    ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> buscarPorLojaEProduto(@PathVariable Long lojaId, @PathVariable Long produtoId);

    @GetMapping
    ResponseEntity<DefaultApiResponse<List<EstoqueResponseDto>>> listarTodos();

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<EstoqueResponseDto>> atualizar(@PathVariable Long id, @Valid @RequestBody EstoqueRequestDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(@PathVariable Long id);
}