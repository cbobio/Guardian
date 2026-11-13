package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbCategoria;
import br.com.cbobio.Guardian.web.dto.request.categoria.CategoriaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.categoria.CategoriaResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public TbCategoria toEntity(CategoriaRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbCategoria.TbCategoriaBuilder tbCategoria = TbCategoria.builder();

        tbCategoria.nome( dto.getNome() );
        tbCategoria.descricao( dto.getDescricao() );
        tbCategoria.altoRisco( dto.getAltoRisco() );
        tbCategoria.ativo( dto.getAtivo() );

        return tbCategoria.build();
    }

    @Override
    public CategoriaResponseDto toResponseDto(TbCategoria entity) {
        if ( entity == null ) {
            return null;
        }

        CategoriaResponseDto.CategoriaResponseDtoBuilder categoriaResponseDto = CategoriaResponseDto.builder();

        categoriaResponseDto.id( entity.getId() );
        categoriaResponseDto.criadoEm( entity.getCriadoEm() );
        categoriaResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        categoriaResponseDto.nome( entity.getNome() );
        categoriaResponseDto.descricao( entity.getDescricao() );
        categoriaResponseDto.altoRisco( entity.getAltoRisco() );
        categoriaResponseDto.ativo( entity.getAtivo() );

        return categoriaResponseDto.build();
    }
}
