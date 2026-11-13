
package br.com.cbobio.Guardian.web.dto.mapper;


import br.com.cbobio.Guardian.model.entity.TbEstoque;
import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.web.dto.request.estoque.EstoqueRequestDto;
import br.com.cbobio.Guardian.web.dto.response.estoque.EstoqueResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstoqueMapper {

    @Mapping(target = "loja", source = "lojaId")
    @Mapping(target = "produto", source = "produtoId")
    TbEstoque toEntity(EstoqueRequestDto dto);

    @Mapping(target = "lojaId", source = "loja.id")
    @Mapping(target = "produtoId", source = "produto.id")
    EstoqueResponseDto toResponseDto(TbEstoque entity);

    default TbLoja mapLoja(Long lojaId) {

        if (lojaId == null) {
            return null;
        }

        TbLoja loja = new TbLoja();
        loja.setId(lojaId);

        return loja;
    }

    default TbProduto mapProduto(Long produtoId) {

        if (produtoId == null) {
            return null;
        }

        TbProduto produto = new TbProduto();
        produto.setId(produtoId);

        return produto;
    }
}