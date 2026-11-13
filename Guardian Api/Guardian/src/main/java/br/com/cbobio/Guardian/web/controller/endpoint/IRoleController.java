
package br.com.cbobio.Guardian.web.controller.endpoint;

import br.com.cbobio.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import br.com.cbobio.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/api/v1/roles")
public interface IRoleController {

    @PostMapping
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> cadastrar(
            @Valid @RequestBody RoleRequestDto dto
    );

    @GetMapping("/{id}")
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> buscarPorId(
            @PathVariable Long id
    );

    @GetMapping
    ResponseEntity<DefaultApiResponse<List<RoleResponseDto>>> listarTodos();

    @PutMapping("/{id}")
    ResponseEntity<DefaultApiResponse<RoleResponseDto>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDto dto
    );

    @DeleteMapping("/{id}")
    ResponseEntity<DefaultApiResponse<String>> remover(
            @PathVariable Long id
    );
}