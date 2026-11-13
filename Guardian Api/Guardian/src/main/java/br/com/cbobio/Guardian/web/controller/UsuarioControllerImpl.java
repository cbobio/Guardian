
package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.UsuarioService;
import br.com.cbobio.Guardian.web.controller.endpoint.IUsuarioController;
import br.com.cbobio.Guardian.web.dto.request.usuario.FilterUsuarioDto;
import br.com.cbobio.Guardian.web.dto.request.usuario.UsuarioRequestDto;
import br.com.cbobio.Guardian.web.dto.response.usuario.UsuarioResponseDto;
import br.com.cbobio.Guardian.web.interceptor.ApiResponseData;
import br.com.cbobio.Guardian.web.interceptor.DefaultApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsuarioControllerImpl implements IUsuarioController {

    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> cadastrar(
            UsuarioRequestDto dto
    ) {

        UsuarioResponseDto response = usuarioService.cadastrar(dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> buscarPorId(
            Long id
    ) {

        UsuarioResponseDto response = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<
            DefaultApiResponse<Page<UsuarioResponseDto>>> listarTodos(
            FilterUsuarioDto filtro,
            Pageable pageable) {

        Page<UsuarioResponseDto> response =
                usuarioService.listarTodos(filtro, pageable);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<UsuarioResponseDto>> atualizar(
            Long id,
            UsuarioRequestDto dto
    ) {

        UsuarioResponseDto response = usuarioService.atualizar(id, dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> remover(Long id) {

        usuarioService.remover(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>("Usuário removido com sucesso.")
                )
        );
    }
}