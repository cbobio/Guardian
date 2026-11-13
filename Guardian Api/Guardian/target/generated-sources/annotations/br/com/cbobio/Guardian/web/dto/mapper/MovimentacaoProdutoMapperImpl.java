package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.web.dto.request.movimentacao.MovimentacaoProdutoRequestDto;
import br.com.cbobio.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class MovimentacaoProdutoMapperImpl implements MovimentacaoProdutoMapper {

    @Override
    public TbMovimentacaoProduto toEntity(MovimentacaoProdutoRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbMovimentacaoProduto.TbMovimentacaoProdutoBuilder tbMovimentacaoProduto = TbMovimentacaoProduto.builder();

        tbMovimentacaoProduto.produto( mapProduto( dto.getProdutoId() ) );
        tbMovimentacaoProduto.loja( mapLoja( dto.getLojaId() ) );
        tbMovimentacaoProduto.usuario( mapUsuario( dto.getUsuarioId() ) );
        tbMovimentacaoProduto.evento( dto.getEvento() );
        tbMovimentacaoProduto.estado( dto.getEstado() );
        tbMovimentacaoProduto.motivo( dto.getMotivo() );
        tbMovimentacaoProduto.quantidade( dto.getQuantidade() );
        tbMovimentacaoProduto.observacao( dto.getObservacao() );
        tbMovimentacaoProduto.estadoAnterior( dto.getEstadoAnterior() );

        return tbMovimentacaoProduto.build();
    }

    @Override
    public MovimentacaoProdutoResponseDto toResponseDto(TbMovimentacaoProduto entity) {
        if ( entity == null ) {
            return null;
        }

        MovimentacaoProdutoResponseDto.MovimentacaoProdutoResponseDtoBuilder movimentacaoProdutoResponseDto = MovimentacaoProdutoResponseDto.builder();

        movimentacaoProdutoResponseDto.produtoId( entityProdutoId( entity ) );
        movimentacaoProdutoResponseDto.lojaId( entityLojaId( entity ) );
        movimentacaoProdutoResponseDto.usuarioId( entityUsuarioId( entity ) );
        movimentacaoProdutoResponseDto.id( entity.getId() );
        movimentacaoProdutoResponseDto.evento( entity.getEvento() );
        movimentacaoProdutoResponseDto.estado( entity.getEstado() );
        movimentacaoProdutoResponseDto.motivo( entity.getMotivo() );
        movimentacaoProdutoResponseDto.quantidade( entity.getQuantidade() );
        movimentacaoProdutoResponseDto.dataMovimentacao( entity.getDataMovimentacao() );
        movimentacaoProdutoResponseDto.observacao( entity.getObservacao() );
        movimentacaoProdutoResponseDto.estadoAnterior( entity.getEstadoAnterior() );

        return movimentacaoProdutoResponseDto.build();
    }

    private Long entityProdutoId(TbMovimentacaoProduto tbMovimentacaoProduto) {
        TbProduto produto = tbMovimentacaoProduto.getProduto();
        if ( produto == null ) {
            return null;
        }
        return produto.getId();
    }

    private Long entityLojaId(TbMovimentacaoProduto tbMovimentacaoProduto) {
        TbLoja loja = tbMovimentacaoProduto.getLoja();
        if ( loja == null ) {
            return null;
        }
        return loja.getId();
    }

    private Long entityUsuarioId(TbMovimentacaoProduto tbMovimentacaoProduto) {
        TbUsuario usuario = tbMovimentacaoProduto.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        return usuario.getId();
    }
}
