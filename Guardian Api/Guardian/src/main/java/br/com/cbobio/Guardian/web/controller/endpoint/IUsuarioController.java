
package br.com.cbobio.Guardian.web.controller.endpoint;

import br.com.cbobio.Guardian.web.dto.request.usuario.FilterUsuarioDto;
import br.com.cbobio.Guardian.web.dto.request.usuario.UsuarioRequestDto;
import br.com.cbobio.Guardian.web.dto.response.usuario.UsuarioResponseDto;
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
@RequestMapping("/api/v1/usuarios")
public interface IUsuarioController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> cadastrar(
            @Valid @RequestBody UsuarioRequestDto dto
    );

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> buscarPorId(
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<DefaultApiResponse<Page<UsuarioResponseDto>>> listarTodos(
            @ModelAttribute FilterUsuarioDto filtro,
            @PageableDefault(
                    size = 10,
                    sort = "nome",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    );

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDto dto
    );

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(
            @PathVariable Long id
    );
}