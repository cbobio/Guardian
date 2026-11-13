
package br.com.cbobio.Guardian.model.repository.specification;

import br.com.cbobio.Guardian.model.entity.TbUsuario;
import br.com.cbobio.Guardian.web.dto.request.usuario.FilterUsuarioDto;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {

    private UsuarioSpecification(){}

    public static Specification<TbUsuario> fromFiltro(FilterUsuarioDto filter){
        Specification<TbUsuario> spec = Specification.unrestricted();

        if(filter == null){
            return spec;
        }

        if(filter.getMatricula() != null && !filter.getMatricula().isBlank()){
            spec = spec.and(matriculaLike(filter.getMatricula()));
        }

        if(filter.getNome() != null && !filter.getNome().isBlank()){
            spec = spec.and(nomeLike(filter.getNome()));
        }

        if(filter.getEmail() != null && !filter.getEmail().isBlank()){
            spec = spec.and(emailLike(filter.getEmail()));
        }

        if(filter.getAtivo() != null){
            spec = spec.and(ativo(filter.getAtivo()));
        }

        return spec;
    }

    public static Specification<TbUsuario> matriculaLike(String matricula){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("matricula")), "%" + matricula.toLowerCase() + "%");
    }

    public static Specification<TbUsuario> nomeLike(String nome){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<TbUsuario> emailLike(String email){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<TbUsuario> ativo(Boolean ativo){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ativo"), ativo);
    }
}
