
package br.com.cbobio.Guardian.machine.guard.devolverProduto;

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
public class DevolverProdutoGuard
        implements Guard<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    public boolean evaluate(
            StateContext<MachineEstados, MachineEventos> context
    ) {
        Long usuarioId =  (Long)  context.getMessageHeader(
                MachineHeaders.USUARIO_ID
        );

        Long movimentacaoId = (Long) context.getMessageHeader(
                MachineHeaders.MOVIMENTACAO_ID
        );

        Integer quantidade = (Integer) context.getMessageHeader(
                MachineHeaders.QUANTIDADE
        );

        if (usuarioId == null || movimentacaoId == null) {
            log.warn(
                    "Devolução bloqueada: usuário ou movimentação não informados."
            );
            return false;
        }

        if (quantidade == null || quantidade <= 0) {
            log.warn(
                    "Devolução bloqueada: quantidade inválida: {}",
                    quantidade
            );
            return false;
        }

        TbMovimentacaoProduto origem =
                movimentacaoProdutoRepository.findById(movimentacaoId)
                        .orElse(null);

        if (origem == null) {
            log.warn(
                    "Devolução bloqueada: movimentação {} não encontrada.",
                    movimentacaoId
            );
            return false;
        }

        if (origem.getEvento() != MachineEventos.RETIRAR
                && origem.getEvento() != MachineEventos.ENTREGAR) {
            log.warn(
                    "Devolução bloqueada: evento de origem inválido: {}",
                    origem.getEvento()
            );
            return false;
        }

        MachineEstados estado = origem.getEstado();

        if (estado != MachineEstados.RETIRADO
                && estado != MachineEstados.ENTREGUE) {
            log.warn(
                    "Devolução bloqueada: estado {} não permitido.",
                    estado
            );
            return false;
        }

        Integer quantidadeOriginal = origem.getQuantidade();

        if (quantidadeOriginal == null || quantidadeOriginal <= 0) {
            log.warn(
                    "Devolução bloqueada: quantidade original inválida. Origem: {}",
                    movimentacaoId
            );
            return false;
        }

        Long quantidadeJaDevolvida =
                movimentacaoProdutoRepository
                        .somarQuantidadePorOrigemEEvento(
                                movimentacaoId,
                                MachineEventos.DEVOLVER
                        );

        if (quantidadeJaDevolvida == null) {
            quantidadeJaDevolvida = 0L;
        }

        long quantidadeDisponivel = Math.max(
                0L,
                quantidadeOriginal.longValue() - quantidadeJaDevolvida
        );

        if (quantidade > quantidadeDisponivel) {
            log.warn(
                    "Devolução bloqueada. Origem: {}, disponível: {}, solicitada: {}",
                    movimentacaoId,
                    quantidadeDisponivel,
                    quantidade
            );
            return false;
        }

        log.info(
                "Guard de devolução aprovado. Usuário: {}, origem: {}, estado: {}, quantidade: {}",
                usuarioId,
                movimentacaoId,
                estado,
                quantidade
        );

        return true;
    }
}