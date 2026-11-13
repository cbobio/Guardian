

package br.com.cbobio.Guardian.model.repository;

import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovimentacaoProdutoRepository
        extends JpaRepository<TbMovimentacaoProduto, Long> {

    boolean existsByProdutoIdAndLojaIdAndEstadoAndEvento(
            Long produtoId,
            Long lojaId,
            MachineEstados estado,
            MachineEventos evento
    );

    boolean existsByProdutoIdAndLojaIdAndEvento(
            Long produtoId,
            Long lojaId,
            MachineEventos evento
    );

    boolean existsByRetirada_IdAndEvento(
            Long retiradaId,
            MachineEventos evento
    );

    // Verifica se a movimentação de origem já possui
    // um evento específico, como FINALIZAR.
    boolean existsByMovimentacaoOrigem_IdAndEvento(
            Long movimentacaoOrigemId,
            MachineEventos evento
    );

    //calcular o total já devolvido
    @Query("""
            SELECT COALESCE(SUM(m.quantidade), 0)
            FROM TbMovimentacaoProduto m
            WHERE m.movimentacaoOrigem.id = :origemId
              AND m.evento = :evento
            """)
    Long somarQuantidadePorOrigemEEvento(
            @Param("origemId") Long origemId,
            @Param("evento") MachineEventos evento
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT m
        FROM TbMovimentacaoProduto m
        WHERE m.id = :id
        """)
    Optional<TbMovimentacaoProduto> buscarPorIdComBloqueio(
            @Param("id") Long id
    );


}