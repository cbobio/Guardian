package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbRole;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.web.dto.request.usuario.UsuarioRequestDto;
import br.com.cbobio.Guardian.web.dto.response.usuario.UsuarioResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public TbUsuario toEntity(UsuarioRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbUsuario.TbUsuarioBuilder tbUsuario = TbUsuario.builder();

        tbUsuario.loja( mapLoja( dto.getLojaId() ) );
        tbUsuario.role( mapRole( dto.getRoleId() ) );
        tbUsuario.matricula( dto.getMatricula() );
        tbUsuario.nome( dto.getNome() );
        tbUsuario.email( dto.getEmail() );
        tbUsuario.senha( dto.getSenha() );
        tbUsuario.ativo( dto.getAtivo() );
        tbUsuario.bloqueado( dto.getBloqueado() );

        return tbUsuario.build();
    }

    @Override
    public UsuarioResponseDto toResponseDto(TbUsuario entity) {
        if ( entity == null ) {
            return null;
        }

        UsuarioResponseDto.UsuarioResponseDtoBuilder usuarioResponseDto = UsuarioResponseDto.builder();

        usuarioResponseDto.lojaId( entityLojaId( entity ) );
        usuarioResponseDto.roleId( entityRoleId( entity ) );
        usuarioResponseDto.id( entity.getId() );
        usuarioResponseDto.criadoEm( entity.getCriadoEm() );
        usuarioResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        usuarioResponseDto.matricula( entity.getMatricula() );
        usuarioResponseDto.nome( entity.getNome() );
        usuarioResponseDto.email( entity.getEmail() );
        usuarioResponseDto.ativo( entity.getAtivo() );
        usuarioResponseDto.bloqueado( entity.getBloqueado() );
        usuarioResponseDto.ultimoLogin( entity.getUltimoLogin() );

        return usuarioResponseDto.build();
    }

    private Long entityLojaId(TbUsuario tbUsuario) {
        TbLoja loja = tbUsuario.getLoja();
        if ( loja == null ) {
            return null;
        }
        return loja.getId();
    }

    private Long entityRoleId(TbUsuario tbUsuario) {
        TbRole role = tbUsuario.getRole();
        if ( role == null ) {
            return null;
        }
        return role.getId();
    }
}
