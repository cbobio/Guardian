
package br.com.cbobio.Guardian.web.controller.endpoint;

import br.com.cbobio.Guardian.web.dto.request.categoria.CategoriaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.categoria.CategoriaResponseDto;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/categorias")
public interface ICategoriaController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> cadastrar(
            @Valid @RequestBody CategoriaRequestDto dto
    );

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> buscarPorId(
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<DefaultApiResponse<List<CategoriaResponseDto>>> listarTodos();

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<CategoriaResponseDto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDto dto
    );

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(
            @PathVariable Long id
    );
}