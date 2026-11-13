
package br.com.cbobio.Guardian.model.repository;

import br.com.cbobio.Guardian.enums.Roles;
import br.com.cbobio.Guardian.model.entity.TbRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<TbRole, Long> {
    boolean existsByRole(Roles role);

    boolean existsByRoleAndIdNot(Roles role, Long id);
}
