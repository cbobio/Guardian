
package br.com.cbobio.Guardian.web.controller;

import br.com.cbobio.Guardian.service.LojaService;
import br.com.cbobio.Guardian.web.controller.endpoint.ILojaController;
import br.com.cbobio.Guardian.web.dto.request.loja.FilterLojaDto;
import br.com.cbobio.Guardian.web.dto.request.loja.LojaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.loja.LojaResponseDto;
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
public class LojaControllerImpl implements ILojaController {

    private final LojaService lojaService;

    @Override
    public ResponseEntity<DefaultApiResponse<LojaResponseDto>> cadastrar(
            LojaRequestDto dto) {

        LojaResponseDto response =
                lojaService.cadastrar(dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<LojaResponseDto>> buscarPorId(
            Long id) {

        LojaResponseDto response =
                lojaService.buscarPorId(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }


    @Override
    public ResponseEntity<DefaultApiResponse<Page<LojaResponseDto>>> listarTodos(
            FilterLojaDto filtro,
            Pageable pageable) {

        Page<LojaResponseDto> response =
                lojaService.listarTodos(filtro, pageable);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<LojaResponseDto>> atualizar(
            Long id,
            LojaRequestDto dto) {

        LojaResponseDto response =
                lojaService.atualizar(id, dto);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(response)
                )
        );
    }

    @Override
    public ResponseEntity<DefaultApiResponse<String>> remover(
            Long id) {

        lojaService.remover(id);

        return ResponseEntity.ok(
                DefaultApiResponse.success(
                        new ApiResponseData<>(
                                "Loja removida com sucesso."
                        )
                )
        );
    }
}