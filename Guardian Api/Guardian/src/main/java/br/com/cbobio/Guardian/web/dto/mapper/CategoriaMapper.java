
package br.com.cbobio.Guardian.web.dto.mapper;


import br.com.cbobio.Guardian.model.entity.TbCategoria;
import br.com.cbobio.Guardian.web.dto.request.categoria.CategoriaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.categoria.CategoriaResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    TbCategoria toEntity(CategoriaRequestDto dto);

    CategoriaResponseDto toResponseDto(TbCategoria entity);
}