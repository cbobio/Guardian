
package br.com.cbobio.Guardian.machine.action;

import br.com.cbobio.Guardian.enums.MotivoMovimentacao;
import br.com.cbobio.Guardian.machine.enums.enumEvent.MachineEventos;
import br.com.cbobio.Guardian.machine.enums.enumState.MachineEstados;
import br.com.cbobio.Guardian.machine.guard.MachineHeaders;
import br.com.cbobio.Guardian.model.entity.TbMovimentacaoProduto;
import br.com.cbobio.Guardian.model.repository.MovimentacaoProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PerderProdutoAction
        implements Action<MachineEstados, MachineEventos> {

    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @Override
    @Transactional
    public void execute(
            StateContext<MachineEstados, MachineEventos> context
    ) {

        if (context.getMessage() == null) {
            throw new IllegalArgumentException(
                    "Mensagem não informada para registrar a perda."
            );
        }

        Long usuarioId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.USUARIO_ID, Long.class);

        Long movimentacaoId = context.getMessage()
                .getHeaders()
                .get(MachineHeaders.MOVIMENTACAO_ID, Long.class);

        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "Usuário e movimentação são obrigatórios para registrar a perda."
            );
        }

        TbMovimentacaoProduto entrega =
                movimentacaoProdutoRepository.findById(movimentacaoId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Movimentação não encontrada: "
                                                + movimentacaoId
                                )
                        );

        if (entrega.getEvento() != MachineEventos.ENTREGAR
                || entrega.getEstado() != MachineEstados.ENTREGUE) {

            throw new IllegalStateException(
                    "Somente uma movimentação de entrega no estado ENTREGUE "
                            + "pode gerar uma perda."
            );
        }

        if (entrega.getRetirada() == null) {
            throw new IllegalStateException(
                    "A entrega informada não possui uma retirada de origem."
            );
        }

        TbMovimentacaoProduto retirada = entrega.getRetirada();

        if (retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {

            throw new IllegalStateException(
                    "A retirada original não é válida para registrar a perda."
            );
        }

        if (entrega.getUsuario() == null
                || !entrega.getUsuario().getId().equals(usuarioId)) {

            throw new IllegalArgumentException(
                    "O usuário informado não corresponde à movimentação original."
            );
        }

        boolean perdaExistente =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retirada.getId(),
                        MachineEventos.PERDER
                );

        if (perdaExistente) {
            throw new IllegalStateException(
                    "Esta retirada já possui uma perda registrada."
            );
        }

        TbMovimentacaoProduto perda = TbMovimentacaoProduto.builder()
                .evento(MachineEventos.PERDER)
                .estado(MachineEstados.PERDIDO)
                .estadoAnterior(MachineEstados.ENTREGUE)
                .motivo(MotivoMovimentacao.PERDA)
                .quantidade(entrega.getQuantidade())
                .produto(entrega.getProduto())
                .loja(entrega.getLoja())
                .usuario(entrega.getUsuario())
                .retirada(retirada)
                .build();

        movimentacaoProdutoRepository.saveAndFlush(perda);

        log.info(
                "Perda registrada. Entrega: {}, retirada original: {}, "
                        + "produto: {}, loja: {}",
                entrega.getId(),
                retirada.getId(),
                entrega.getProduto().getId(),
                entrega.getLoja().getId()
        );
    }
}