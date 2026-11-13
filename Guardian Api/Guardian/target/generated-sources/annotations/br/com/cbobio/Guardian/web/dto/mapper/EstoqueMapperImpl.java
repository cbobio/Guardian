package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbEstoque;
import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.web.dto.request.estoque.EstoqueRequestDto;
import br.com.cbobio.Guardian.web.dto.response.estoque.EstoqueResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class EstoqueMapperImpl implements EstoqueMapper {

    @Override
    public TbEstoque toEntity(EstoqueRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbEstoque.TbEstoqueBuilder tbEstoque = TbEstoque.builder();

        tbEstoque.loja( mapLoja( dto.getLojaId() ) );
        tbEstoque.produto( mapProduto( dto.getProdutoId() ) );
        tbEstoque.quantidade( dto.getQuantidade() );
        tbEstoque.quantidadeMinima( dto.getQuantidadeMinima() );

        return tbEstoque.build();
    }

    @Override
    public EstoqueResponseDto toResponseDto(TbEstoque entity) {
        if ( entity == null ) {
            return null;
        }

        EstoqueResponseDto.EstoqueResponseDtoBuilder estoqueResponseDto = EstoqueResponseDto.builder();

        estoqueResponseDto.lojaId( entityLojaId( entity ) );
        estoqueResponseDto.produtoId( entityProdutoId( entity ) );
        estoqueResponseDto.id( entity.getId() );
        estoqueResponseDto.criadoEm( entity.getCriadoEm() );
        estoqueResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        estoqueResponseDto.quantidade( entity.getQuantidade() );
        estoqueResponseDto.quantidadeMinima( entity.getQuantidadeMinima() );

        return estoqueResponseDto.build();
    }

    private Long entityLojaId(TbEstoque tbEstoque) {
        TbLoja loja = tbEstoque.getLoja();
        if ( loja == null ) {
            return null;
        }
        return loja.getId();
    }

    private Long entityProdutoId(TbEstoque tbEstoque) {
        TbProduto produto = tbEstoque.getProduto();
        if ( produto == null ) {
            return null;
        }
        return produto.getId();
    }
}
