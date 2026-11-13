package br.com.cbobio.Guardian.web.dto.mapper;

import br.com.cbobio.Guardian.model.entity.TbLoja;
import br.com.cbobio.Guardian.web.dto.request.loja.LojaRequestDto;
import br.com.cbobio.Guardian.web.dto.response.loja.LojaResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-13T15:43:58-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.5.0.3 (Ubuntu)"
)
@Component
public class LojaMapperImpl implements LojaMapper {

    @Override
    public TbLoja toEntity(LojaRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        TbLoja.TbLojaBuilder tbLoja = TbLoja.builder();

        tbLoja.numeroLoja( dto.getNumeroLoja() );
        tbLoja.nomeFantasia( dto.getNomeFantasia() );
        tbLoja.razaoSocial( dto.getRazaoSocial() );
        tbLoja.cnpj( dto.getCnpj() );
        tbLoja.logradouro( dto.getLogradouro() );
        tbLoja.numero( dto.getNumero() );
        tbLoja.bairro( dto.getBairro() );
        tbLoja.cidade( dto.getCidade() );
        tbLoja.uf( dto.getUf() );
        tbLoja.cep( dto.getCep() );
        tbLoja.telefone( dto.getTelefone() );
        tbLoja.email( dto.getEmail() );
        tbLoja.ativo( dto.getAtivo() );
        tbLoja.controleProdutosAltoRisco( dto.getControleProdutosAltoRisco() );

        return tbLoja.build();
    }

    @Override
    public LojaResponseDto toResponseDto(TbLoja entity) {
        if ( entity == null ) {
            return null;
        }

        LojaResponseDto.LojaResponseDtoBuilder lojaResponseDto = LojaResponseDto.builder();

        lojaResponseDto.id( entity.getId() );
        lojaResponseDto.criadoEm( entity.getCriadoEm() );
        lojaResponseDto.atualizadoEm( entity.getAtualizadoEm() );
        lojaResponseDto.numeroLoja( entity.getNumeroLoja() );
        lojaResponseDto.nomeFantasia( entity.getNomeFantasia() );
        lojaResponseDto.razaoSocial( entity.getRazaoSocial() );
        lojaResponseDto.cnpj( entity.getCnpj() );
        lojaResponseDto.logradouro( entity.getLogradouro() );
        lojaResponseDto.numero( entity.getNumero() );
        lojaResponseDto.bairro( entity.getBairro() );
        lojaResponseDto.cidade( entity.getCidade() );
        lojaResponseDto.uf( entity.getUf() );
        lojaResponseDto.cep( entity.getCep() );
        lojaResponseDto.telefone( entity.getTelefone() );
        lojaResponseDto.email( entity.getEmail() );
        lojaResponseDto.ativo( entity.getAtivo() );
        lojaResponseDto.controleProdutosAltoRisco( entity.getControleProdutosAltoRisco() );

        return lojaResponseDto.build();
    }
}
