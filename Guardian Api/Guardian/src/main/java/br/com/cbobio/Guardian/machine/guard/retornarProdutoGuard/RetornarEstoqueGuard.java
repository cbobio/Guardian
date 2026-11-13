
package br.com.cbobio.Guardian.machine.guard.retornarProdutoGuard;

import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import br.com.cbobio.Guardian.machine.guard.MachineHeaders;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import br.com.cbobio.Guardian.model.repository.MovimentacaoProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetornarEstoqueGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    public boolean evaluate(StateContext<MachineEstados, MachineEventos> context) {

        log.info("Guard RETORNAR_ESTOQUE iniciado.");

        if (context.getMessage() == null) {
            log.warn("Retorno rejeitado: mensagem não encontrada.");
            return false;
        }

        Long movimentacaoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.MOVIMENTACAO_ID, Long.class);

        log.info("ID da movimentação recebido: {}", movimentacaoId);

        if (movimentacaoId == null) {
            log.warn("Retorno rejeitado: movimentação não informada.");
            return false;
        }

        TbMovimentacaoProduto devolucao = movimentacaoProdutoRepository
                .findById(movimentacaoId)
                .orElse(null);

        if (devolucao == null) {
            log.warn(
                    "Retorno rejeitado: movimentação {} não encontrada.",
                    movimentacaoId
            );
            return false;
        }

        log.info(
                "Movimentação encontrada: evento={}, estado={}, estadoMáquina={}",
                devolucao.getEvento(),
                devolucao.getEstado(),
                context.getSource() != null
                        ? context.getSource().getId()
                        : null
        );

        boolean origemValida =
                devolucao.getEvento() == MachineEventos.DEVOLVER
                        && devolucao.getEstado() == MachineEstados.DEVOLVIDO
                        && context.getSource() != null
                        && context.getSource().getId() == MachineEstados.DEVOLVIDO;

        if (!origemValida) {
            log.warn("Retorno rejeitado: origem inválida.");
            return false;
        }

        boolean jaRetornouAoEstoque =
                movimentacaoProdutoRepository
                        .existsByMovimentacaoOrigem_IdAndEvento(
                                movimentacaoId,
                                MachineEventos.RETORNAR_ESTOQUE
                        );

        log.info(
                "Devolução {} já possui retorno ao estoque: {}",
                movimentacaoId,
                jaRetornouAoEstoque
        );

        if (jaRetornouAoEstoque) {
            log.warn("Retorno rejeitado: devolução já processada.");
            return false;
        }

        log.info("Guard RETORNAR_ESTOQUE aprovado.");
        return true;
    }
}