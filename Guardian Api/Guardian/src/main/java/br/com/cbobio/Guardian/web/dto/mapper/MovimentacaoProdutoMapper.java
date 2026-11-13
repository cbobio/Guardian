

package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.web.dto.request.movimentacao.MovimentacaoProdutoRequestDto;
import br.com.cbobio.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimentacaoProdutoMapper {

    @Mapping(target = "produto", source = "produtoId")
    @Mapping(target = "loja", source = "lojaId")
    @Mapping(target = "usuario", source = "usuarioId")
    TbMovimentacaoProduto toEntity(MovimentacaoProdutoRequestDto dto);

    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "lojaId", source = "loja.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    MovimentacaoProdutoResponseDto toResponseDto(TbMovimentacaoProduto entity);

    default TbProduto mapProduto(Long produtoId) {

        if (produtoId == null) {
            return null;
        }

        TbProduto produto = new TbProduto();
        produto.setId(produtoId);

        return produto;
    }

    default TbLoja mapLoja(Long lojaId) {

        if (lojaId == null) {
            return null;
        }

        TbLoja loja = new TbLoja();
        loja.setId(lojaId);

        return loja;
    }

    default TbUsuario mapUsuario(Long usuarioId) {

        if (usuarioId == null) {
            return null;
        }

        TbUsuario usuario = new TbUsuario();
        usuario.setId(usuarioId);

        return usuario;
    }
}