
package br.com.cbobio.Guardian.model.repository.specification;


import br.com.cbobio.Guardian.model.entity.TbProduto;
import br.com.cbobio.Guardian.web.dto.request.produto.FilterProdutoDto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {
    private ProdutoSpecification(){}

    public static Specification<TbProduto> fromFiltro(FilterProdutoDto filter){
        Specification<TbProduto> spec  = Specification.unrestricted();

        if(filter == null){
            return spec;
        }

        if(filter.getNome() != null && !filter.getNome().isBlank()){
            spec = spec.and(nomeLike(filter.getNome()));
        }

        if(filter.getCodigoBarras() != null && !filter.getCodigoBarras().isBlank()){
            spec = spec.and(codigoBarrasLike(filter.getCodigoBarras()));
        }

        if(filter.getDescricao() != null && !filter.getDescricao().isBlank()){
            spec = spec.and(descricaoLike(filter.getDescricao()));
        }

        if(filter.getMarca() != null && !filter.getMarca().isBlank()){
            spec = spec.and(marcaLike(filter.getMarca()));
        }

        return spec;
    }

    public static Specification<TbProduto> nomeLike(String nome){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<TbProduto> codigoBarrasLike(String codigoBarras){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("codigoBarras")), "%" + codigoBarras.toLowerCase() + "%");
    }

    public static Specification<TbProduto> descricaoLike(String descricao){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
    }

    public static Specification<TbProduto> marcaLike(String marca){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + marca.toLowerCase() + "%");
    }
}
