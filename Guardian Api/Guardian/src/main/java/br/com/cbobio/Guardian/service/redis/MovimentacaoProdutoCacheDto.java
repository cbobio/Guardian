
package br.com.cbobio.Guardian.service.redis;



import br.com.cbobio.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;

import java.util.List;

public record MovimentacaoProdutoCacheDto(
        List<MovimentacaoProdutoResponseDto> conteudo,
        int numeroPagina,
        int tamanhoPagina,
        long totalElementos,
        int totalPaginas
) {
}