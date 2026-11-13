
package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbRole;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.web.dto.request.usuario.UsuarioRequestDto;
import br.com.cbobio.Guardian.web.dto.response.usuario.UsuarioResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "loja", source = "lojaId")
    @Mapping(target = "role", source = "roleId")
    TbUsuario toEntity(UsuarioRequestDto dto);

    @Mapping(target = "lojaId", source = "loja.id")
    @Mapping(target = "roleId", source = "role.id")
    UsuarioResponseDto toResponseDto(TbUsuario entity);

    default TbLoja mapLoja(Long lojaId) {

        if (lojaId == null) {
            return null;
        }

        TbLoja loja = new TbLoja();
        loja.setId(lojaId);

        return loja;
    }

    default TbRole mapRole(Long roleId) {

        if (roleId == null) {
            return null;
        }

        TbRole role = new TbRole();
        role.setId(roleId);

        return role;
    }
}