
package br.com.cbobio.Guardian.model.repository;

import com.atacadao.Guardian.model.entity.TbProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends
        JpaRepository<TbProduto, Long>,
        JpaSpecificationExecutor<TbProduto> {

    Optional<TbProduto> findByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarras(String codigoBarras);
}