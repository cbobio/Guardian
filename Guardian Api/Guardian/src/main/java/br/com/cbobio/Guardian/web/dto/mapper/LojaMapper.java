
package br.com.cbobio.Guardian.web.dto.mapper;


import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.web.dto.request.loja.LojaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.loja.LojaResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LojaMapper {

    TbLoja toEntity(LojaRequestDto dto);

    LojaResponseDto toResponseDto(TbLoja entity);
}