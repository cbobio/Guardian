
package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbCategoria;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.web.dto.request.produto.ProdutoRequestDto;
import br.com.cbobio.Guardian.web.dto.response.produto.ProdutoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "categoria", source = "categoriaId")
    TbProduto toEntity(ProdutoRequestDto dto);

    @Mapping(target = "categoriaId", source = "categoria.id")
    ProdutoResponseDto toResponseDto(TbProduto entity);

    default TbCategoria mapCategoria(Long categoriaId) {

        if (categoriaId == null) {
            return null;
        }

        TbCategoria categoria = new TbCategoria();
        categoria.setId(categoriaId);

        return categoria;
    }
}