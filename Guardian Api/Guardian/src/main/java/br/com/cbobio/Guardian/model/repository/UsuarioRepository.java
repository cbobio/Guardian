
package br.com.cbobio.Guardian.model.repository;

import br.com.cbobio.Guardian.model.entity.TbUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends
        JpaRepository<TbUsuario, Long>,
        JpaSpecificationExecutor<TbUsuario> {

    boolean existsByMatricula(String matricula);

    boolean existsByEmail(String email);

    boolean existsByMatriculaAndIdNot(
            String matricula, Long id);

    boolean existsByEmailAndIdNot(
            String email, Long id);
}