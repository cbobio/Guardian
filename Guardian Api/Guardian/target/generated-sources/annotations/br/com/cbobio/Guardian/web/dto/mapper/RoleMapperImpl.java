package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbRole;
import br.com.cbobio.Guardian.web.dto.request.dtoRole.RoleRequestDto;
import br.com.cbobio.Guardian.web.dto.response.dtoRole.RoleResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:57-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public TbRole toEntity(RoleRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbRole.TbRoleBuilder tbRole = TbRole.builder();

        tbRole.role( dto.getRole() );
        tbRole.descricao( dto.getDescricao() );

        return tbRole.build();
    }

    @Override
    public RoleResponseDto toResponseDto(TbRole entity) {
        if ( entity == null ) {
            return null;
        }

        RoleResponseDto.RoleResponseDtoBuilder roleResponseDto = RoleResponseDto.builder();

        roleResponseDto.id( entity.getId() );
        roleResponseDto.criadoEm( entity.getCriadoEm() );
        roleResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        roleResponseDto.role( entity.getRole() );
        roleResponseDto.descricao( entity.getDescricao() );

        return roleResponseDto.build();
    }
}
