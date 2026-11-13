package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbCategoria;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.web.dto.request.produto.ProdutoRequestDto;
import br.com.cbobio.Guardian.web.dto.response.produto.ProdutoResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public TbProduto toEntity(ProdutoRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbProduto.TbProdutoBuilder tbProduto = TbProduto.builder();

        tbProduto.categoria( mapCategoria( dto.getCategoriaId() ) );
        tbProduto.codigoBarras( dto.getCodigoBarras() );
        tbProduto.nome( dto.getNome() );
        tbProduto.descricao( dto.getDescricao() );
        tbProduto.marca( dto.getMarca() );
        tbProduto.custo( dto.getCusto() );
        tbProduto.altoRisco( dto.getAltoRisco() );
        tbProduto.ativo( dto.getAtivo() );

        return tbProduto.build();
    }

    @Override
    public ProdutoResponseDto toResponseDto(TbProduto entity) {
        if ( entity == null ) {
            return null;
        }

        ProdutoResponseDto.ProdutoResponseDtoBuilder produtoResponseDto = ProdutoResponseDto.builder();

        produtoResponseDto.categoriaId( entityCategoriaId( entity ) );
        produtoResponseDto.id( entity.getId() );
        produtoResponseDto.criadoEm( entity.getCriadoEm() );
        produtoResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        produtoResponseDto.codigoBarras( entity.getCodigoBarras() );
        produtoResponseDto.nome( entity.getNome() );
        produtoResponseDto.descricao( entity.getDescricao() );
        produtoResponseDto.marca( entity.getMarca() );
        produtoResponseDto.custo( entity.getCusto() );
        produtoResponseDto.altoRisco( entity.getAltoRisco() );
        produtoResponseDto.ativo( entity.getAtivo() );

        return produtoResponseDto.build();
    }

    private Long entityCategoriaId(TbProduto tbProduto) {
        TbCategoria categoria = tbProduto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        return categoria.getId();
    }
}
