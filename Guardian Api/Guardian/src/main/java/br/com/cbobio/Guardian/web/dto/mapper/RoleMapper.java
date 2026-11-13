
package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbRole;
import br.com.cbobio.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import br.com.cbobio.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    TbRole toEntity(RoleRequestDto dto);

    RoleResponseDto toResponseDto(TbRole entity);
}