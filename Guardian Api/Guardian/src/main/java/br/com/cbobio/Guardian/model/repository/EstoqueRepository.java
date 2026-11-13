
package br.com.cbobio.Guardian.model.repository;

import br.com.cbobio.Guardian.model.entity.TbEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<TbEstoque, Long> {

    Optional<TbEstoque> findByLojaIdAndProdutoId(Long lojaId, Long produtoId);

}
