
package br.com.cbobio.Guardian.web.controller.endpoint;

import br.com.cbobio.Guardian.web.dto.request.loja.FilterLojaDto;
import br.com.cbobio.Guardian.web.dto.request.loja.LojaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.loja.LojaResponseDto;
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
@RequestMapping("/api/v1/lojas")
public interface ILojaController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> cadastrar(@Valid @RequestBody LojaRequestDto dto);

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> buscarPorId(@PathVariable Long id);

    @GetMapping
    ResponseEntity<DefaultApiResponse<Page<LojaResponseDto>>> listarTodos(
            @ModelAttribute FilterLojaDto filtro,
            @PageableDefault(
                    size = 10,
                    sort = "nomeFantasia",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    );

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<LojaResponseDto>> atualizar(@PathVariable Long id, @Valid @RequestBody LojaRequestDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(@PathVariable Long id);
}