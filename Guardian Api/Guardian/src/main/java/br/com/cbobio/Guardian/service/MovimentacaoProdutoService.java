/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service;

import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.MachineHeaders;
import com.atacadao.Guardian.model.entity.TbMovimentacaoProduto;
import com.atacadao.Guardian.model.repository.MovimentacaoProdutoRepository;
import com.atacadao.Guardian.service.exception.ConflitoException;
import com.atacadao.Guardian.service.exception.QuantidadeSolicitadaException;
import com.atacadao.Guardian.service.redis.MovimentacaoProdutoCacheDto;
import com.atacadao.Guardian.service.redis.RedisService;
import com.atacadao.Guardian.web.dto.mapper.MovimentacaoProdutoMapper;
import com.atacadao.Guardian.web.dto.response.movimentacao.MovimentacaoProdutoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimentacaoProdutoService {

    private final StateMachineFactory<MachineEstados, MachineEventos> stateMachineFactory;
    private final MovimentacaoProdutoRepository movimentacaoProdutoRepository;
    private final MovimentacaoProdutoMapper movimentacaoProdutoMapper;
    private final RedisService redisService;

    @Transactional
    public void retirarProduto(
            Long usuarioId,
            Long produtoId,
            Long lojaId,
            Integer quantidade
    ) {
        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.RETIRAR)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.PRODUTO_ID, produtoId)
                    .setHeader(MachineHeaders.LOJA_ID, lojaId)
                    .setHeader(MachineHeaders.QUANTIDADE, quantidade)
                    .build();

            boolean aceito = stateMachine.sendEvent(message);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.RETIRADO) {
                throw new IllegalStateException(
                        "Não foi possível realizar a retirada do produto."
                );
            }

            log.info(
                    "Retirada realizada. Produto: {}, loja: {}, usuário: {}",
                    produtoId, lojaId, usuarioId
            );
            invalidarCacheMovimentacoesAposCommit();

        } finally {
            stateMachine.stop();
        }
    }

    @Transactional
    public void entregarProduto(
            Long usuarioId,
            Long retiradaId,
            Long produtoId,
            Long lojaId,
            Integer quantidade
    ) {
        TbMovimentacaoProduto retirada = movimentacaoProdutoRepository
                .buscarPorIdComBloqueio(retiradaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Retirada não encontrada: " + retiradaId
                ));

        if (retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {
            throw new IllegalArgumentException(
                    "A movimentação informada não é uma retirada válida."
            );
        }

        if (!retirada.getProduto().getId().equals(produtoId)
                || !retirada.getLoja().getId().equals(lojaId)) {
            throw new IllegalArgumentException(
                    "Produto ou loja não correspondem à retirada informada."
            );
        }

        if (quantidade == null
                || quantidade <= 0
                || quantidade > retirada.getQuantidade()) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser positiva e não pode exceder a quantidade retirada."
            );
        }

        boolean jaFoiEntregue =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retiradaId,
                        MachineEventos.ENTREGAR
                );

        if (jaFoiEntregue) {
            throw new ConflitoException(
                    "Esta retirada já possui uma entrega registrada."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(
                        new DefaultStateMachineContext<>(
                                MachineEstados.RETIRADO,
                                null,
                                null,
                                null
                        )
                )
        );

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.ENTREGAR)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.PRODUTO_ID, produtoId)
                    .setHeader(MachineHeaders.LOJA_ID, lojaId)
                    .setHeader(MachineHeaders.QUANTIDADE, quantidade)
                    .setHeader(MachineHeaders.RETIRADA_ID, retiradaId)
                    .build();

            boolean aceito = stateMachine.sendEvent(message);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.ENTREGUE) {
                throw new IllegalStateException(
                        "Não foi possível realizar a entrega do produto."
                );
            }

            log.info(
                    "Entrega realizada. Retirada: {}, produto: {}, loja: {}, quantidade: {}",
                    retiradaId,
                    produtoId,
                    lojaId,
                    quantidade
            );
            invalidarCacheMovimentacoesAposCommit();
        } finally {
            stateMachine.stop();
        }
    }


    @Transactional(readOnly = true)
    public Page<MovimentacaoProdutoResponseDto> listarTodos(
            Pageable pageable
    ) {
        String chave = "movimentacoes:pagina:"
                + pageable.getPageNumber()
                + ":tamanho:"
                + pageable.getPageSize()
                + ":ordenacao:"
                + pageable.getSort();

        log.info("Consultando cache Redis. Chave: {}", chave);

        MovimentacaoProdutoCacheDto cache =
                redisService.buscarObjeto(
                        chave,
                        MovimentacaoProdutoCacheDto.class
                );

        if (cache != null) {
            log.info("CACHE HIT: {}", chave);

            return new org.springframework.data.domain.PageImpl<>(
                    cache.conteudo(),
                    pageable,
                    cache.totalElementos()
            );
        }

        log.info("CACHE MISS: {}", chave);

        Page<MovimentacaoProdutoResponseDto> pagina =
                movimentacaoProdutoRepository.findAll(pageable)
                        .map(movimentacaoProdutoMapper::toResponseDto);

        MovimentacaoProdutoCacheDto dadosCache =
                new MovimentacaoProdutoCacheDto(
                        pagina.getContent(),
                        pagina.getNumber(),
                        pagina.getSize(),
                        pagina.getTotalElements(),
                        pagina.getTotalPages()
                );

        redisService.salvarObjeto(
                chave,
                dadosCache,
                java.time.Duration.ofMinutes(5)
        );

        log.info("CACHE SAVE: {}", chave);

        return pagina;
    }


    @Transactional
    public void perderProduto(
            Long usuarioId,
            Long movimentacaoId
    ) {
        TbMovimentacaoProduto entrega = movimentacaoProdutoRepository
                .findById(movimentacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimentação não encontrada: " + movimentacaoId
                ));

        if (entrega.getEvento() != MachineEventos.ENTREGAR
                || entrega.getEstado() != MachineEstados.ENTREGUE) {
            throw new IllegalArgumentException(
                    "A movimentação informada não é uma entrega válida."
            );
        }

        TbMovimentacaoProduto retirada = entrega.getRetirada();

        if (retirada == null
                || retirada.getEvento() != MachineEventos.RETIRAR
                || retirada.getEstado() != MachineEstados.RETIRADO) {
            throw new IllegalArgumentException(
                    "A entrega não possui uma retirada original válida."
            );
        }

        boolean jaFoiRegistradaPerda =
                movimentacaoProdutoRepository.existsByRetirada_IdAndEvento(
                        retirada.getId(),
                        MachineEventos.PERDER
                );

        if (jaFoiRegistradaPerda) {
            throw new ConflitoException(
                    "Esta retirada já possui uma perda registrada."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(
                        new DefaultStateMachineContext<>(
                                MachineEstados.ENTREGUE,
                                null,
                                null,
                                null
                        )
                )
        );

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.PERDER)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.MOVIMENTACAO_ID, movimentacaoId)
                    .build();

            boolean aceito = stateMachine.sendEvent(message);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.PERDIDO) {
                throw new IllegalStateException(
                        "Não foi possível registrar a perda do produto."
                );
            }

            log.info(
                    "Perda registrada. Movimentação: {}, retirada: {}, usuário: {}",
                    movimentacaoId,
                    retirada.getId(),
                    usuarioId
            );
            invalidarCacheMovimentacoesAposCommit();

        } finally {
            stateMachine.stop();
        }
    }

    @Transactional
    public void avariarProduto(
            Long usuarioId,
            Long movimentacaoId,
            Integer quantidade
    ) {
        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "O usuário e a movimentação são obrigatórios."
            );
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade avariada deve ser maior que zero."
            );
        }

        TbMovimentacaoProduto origem = movimentacaoProdutoRepository
                .buscarPorIdComBloqueio(movimentacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimentação não encontrada: " + movimentacaoId
                ));

        MachineEstados estadoOrigem = origem.getEstado();

        if (estadoOrigem != MachineEstados.ESTOQUE
                && estadoOrigem != MachineEstados.RETIRADO
                && estadoOrigem != MachineEstados.ENTREGUE) {
            throw new IllegalArgumentException(
                    "A movimentação informada não permite registrar avaria."
            );
        }

        Integer quantidadeOrigem = origem.getQuantidade();

        if (quantidadeOrigem == null || quantidadeOrigem <= 0) {
            throw new IllegalArgumentException(
                    "A movimentação de origem não possui quantidade válida."
            );
        }

        Long quantidadeJaAvariada =
                movimentacaoProdutoRepository.somarQuantidadePorOrigemEEvento(
                        movimentacaoId,
                        MachineEventos.AVARIAR
                );

        if (quantidadeJaAvariada == null) {
            quantidadeJaAvariada = 0L;
        }

        long quantidadeDisponivel = Math.max(
                0L,
                quantidadeOrigem.longValue() - quantidadeJaAvariada
        );

        if (quantidade > quantidadeDisponivel) {
            throw new QuantidadeSolicitadaException(
                    "Quantidade solicitada excede o saldo disponível para avaria. "
                            + "Quantidade disponível: " + quantidadeDisponivel + "."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(
                        new DefaultStateMachineContext<>(
                                estadoOrigem,
                                null,
                                null,
                                null
                        )
                )
        );

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.AVARIAR)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.MOVIMENTACAO_ID, movimentacaoId)
                    .setHeader(MachineHeaders.QUANTIDADE, quantidade)
                    .build();

            boolean aceito = stateMachine.sendEvent(message);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.AVARIADO) {
                throw new IllegalStateException(
                        "Não foi possível registrar a avaria do produto."
                );
            }

            log.info(
                    "Avaria registrada. Movimentação de origem: {}, produto: {}, loja: {}, quantidade: {}, usuário: {}",
                    movimentacaoId,
                    origem.getProduto().getId(),
                    origem.getLoja().getId(),
                    quantidade,
                    usuarioId
            );
            invalidarCacheMovimentacoesAposCommit();

        } finally {
            stateMachine.stop();
        }
    }

    @Transactional
    public void devolverProduto(
            Long usuarioId,
            Long movimentacaoId,
            Integer quantidade,
            String observacao
    ) {
        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "O usuário e a movimentação são obrigatórios."
            );
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade devolvida deve ser maior que zero."
            );
        }

        TbMovimentacaoProduto origem = movimentacaoProdutoRepository
                .buscarPorIdComBloqueio(movimentacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimentação não encontrada: " + movimentacaoId
                ));

        MachineEstados estadoOrigem = origem.getEstado();

        if (estadoOrigem != MachineEstados.RETIRADO
                && estadoOrigem != MachineEstados.ENTREGUE) {
            throw new IllegalArgumentException(
                    "A movimentação informada não permite devolução."
            );
        }

        Integer quantidadeOrigem = origem.getQuantidade();

        if (quantidadeOrigem == null || quantidadeOrigem <= 0) {
            throw new IllegalArgumentException(
                    "A movimentação de origem não possui quantidade válida."
            );
        }

        Long quantidadeJaDevolvida =
                movimentacaoProdutoRepository.somarQuantidadePorOrigemEEvento(
                        movimentacaoId,
                        MachineEventos.DEVOLVER
                );

        if (quantidadeJaDevolvida == null) {
            quantidadeJaDevolvida = 0L;
        }

        long quantidadeDisponivel = Math.max(
                0L,
                quantidadeOrigem.longValue() - quantidadeJaDevolvida
        );

        if (quantidade > quantidadeDisponivel) {
            throw new QuantidadeSolicitadaException(
                    "Quantidade solicitada excede o saldo disponível para devolução. "
                            + "Quantidade disponível: " + quantidadeDisponivel + "."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(
                        new DefaultStateMachineContext<>(
                                estadoOrigem,
                                null,
                                null,
                                null
                        )
                )
        );

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.DEVOLVER)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.MOVIMENTACAO_ID, movimentacaoId)
                    .setHeader(MachineHeaders.QUANTIDADE, quantidade)
                    .setHeader(MachineHeaders.OBSERVACAO, observacao)
                    .build();

            boolean aceito = stateMachine.sendEvent(message);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.DEVOLVIDO) {
                throw new IllegalStateException(
                        "Não foi possível registrar a devolução do produto."
                );
            }

            log.info(
                    "Devolução registrada. Origem: {}, quantidade: {}, usuário: {}",
                    movimentacaoId,
                    quantidade,
                    usuarioId
            );
            invalidarCacheMovimentacoesAposCommit();

        } finally {
            stateMachine.stop();
        }
    }

    @Transactional
    public void retornarProdutoAoEstoque(
            Long usuarioId,
            Long movimentacaoId
    ) {
        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "O usuário e a movimentação são obrigatórios."
            );
        }

        // 1. Bloqueia a devolução até o fim da transação.
        TbMovimentacaoProduto devolucao = movimentacaoProdutoRepository
                .buscarPorIdComBloqueio(movimentacaoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimentação não encontrada: " + movimentacaoId
                ));

        // 2. Valida o evento e o estado da devolução.
        if (devolucao.getEvento() != MachineEventos.DEVOLVER
                || devolucao.getEstado() != MachineEstados.DEVOLVIDO) {
            throw new IllegalArgumentException(
                    "Somente uma devolução no estado DEVOLVIDO "
                            + "pode retornar ao estoque."
            );
        }

        // 3. Verifica se o retorno já foi registrado.
        boolean jaRetornouAoEstoque =
                movimentacaoProdutoRepository
                        .existsByMovimentacaoOrigem_IdAndEvento(
                                movimentacaoId,
                                MachineEventos.RETORNAR_ESTOQUE
                        );

        if (jaRetornouAoEstoque) {
            throw new ConflitoException(
                    "Esta devolução já possui um retorno ao estoque registrado."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                access.resetStateMachine(
                        new DefaultStateMachineContext<>(
                                MachineEstados.DEVOLVIDO,
                                null,
                                null,
                                null
                        )
                )
        );

        try {
            stateMachine.start();

            Message<MachineEventos> message = MessageBuilder
                    .withPayload(MachineEventos.RETORNAR_ESTOQUE)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.MOVIMENTACAO_ID, movimentacaoId)
                    .build();

            List<StateMachineEventResult<MachineEstados, MachineEventos>>
                    resultados = stateMachine.sendEvent(Mono.just(message))
                    .collectList()
                    .block();

            boolean aceito = resultados != null
                    && !resultados.isEmpty()
                    && resultados.stream().allMatch(resultado ->
                    resultado.getResultType()
                            == StateMachineEventResult.ResultType.ACCEPTED
            );

            if (!aceito) {
                throw new IllegalStateException(
                        "O evento RETORNAR_ESTOQUE não foi aceito pela máquina de estados."
                );
            }

            if (stateMachine.getState() == null
                    || stateMachine.getState().getId() != MachineEstados.ESTOQUE) {
                throw new IllegalStateException(
                        "A máquina não chegou ao estado ESTOQUE após o retorno."
                );
            }

            log.info(
                    "Retorno ao estoque realizado. Devolução: {}, usuário: {}",
                    movimentacaoId,
                    usuarioId
            );
            invalidarCacheMovimentacoesAposCommit();
        } finally {
            stateMachine.stop();
        }
    }

    @Transactional
    public String finalizarProduto(
            Long usuarioId,
            Long movimentacaoId,
            String observacao
    ) {
        if (usuarioId == null || movimentacaoId == null) {
            throw new IllegalArgumentException(
                    "Usuário e movimentação são obrigatórios para finalizar."
            );
        }

        TbMovimentacaoProduto origem =
                movimentacaoProdutoRepository
                        .buscarPorIdComBloqueio(movimentacaoId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Movimentação de entrega não encontrada."
                        ));

        if (origem.getEvento() != MachineEventos.ENTREGAR
                || origem.getEstado() != MachineEstados.ENTREGUE) {
            throw new IllegalStateException(
                    "Somente uma entrega concluída pode ser finalizada."
            );
        }

        boolean jaFinalizada =
                movimentacaoProdutoRepository
                        .existsByMovimentacaoOrigem_IdAndEvento(
                                movimentacaoId,
                                MachineEventos.FINALIZAR
                        );

        if (jaFinalizada) {
            throw new ConflitoException(
                    "Esta entrega já possui uma finalização registrada."
            );
        }

        StateMachine<MachineEstados, MachineEventos> stateMachine =
                stateMachineFactory.getStateMachine();

        try {
            stateMachine.getStateMachineAccessor().doWithAllRegions(access ->
                    access.resetStateMachine(
                            new DefaultStateMachineContext<>(
                                    MachineEstados.ENTREGUE,
                                    null,
                                    null,
                                    null
                            )
                    )
            );

            stateMachine.start();

            Message<MachineEventos> mensagem = MessageBuilder
                    .withPayload(MachineEventos.FINALIZAR)
                    .setHeader(MachineHeaders.USUARIO_ID, usuarioId)
                    .setHeader(MachineHeaders.MOVIMENTACAO_ID, movimentacaoId)
                    .setHeader(MachineHeaders.OBSERVACAO, observacao)
                    .build();

            boolean aceito = stateMachine.sendEvent(mensagem);

            if (!aceito
                    || stateMachine.getState() == null
                    || stateMachine.getState().getId()
                    != MachineEstados.FINALIZADO) {
                throw new IllegalStateException(
                        "Não foi possível finalizar a movimentação."
                );
            }

            log.info(
                    "Movimentação finalizada. Origem: {}, usuário: {}",
                    movimentacaoId,
                    usuarioId
            );

            invalidarCacheMovimentacoesAposCommit();

            return "Movimentação finalizada com sucesso.";

        } finally {
            stateMachine.stop();
        }
    }

    private void invalidarCacheMovimentacoesAposCommit() {

        if (TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {

                        @Override
                        public void afterCommit() {
                            redisService.removerPorPrefixo(
                                    "movimentacoes:pagina:"
                            );
                        }
                    }
            );

        } else {
            redisService.removerPorPrefixo("movimentacoes:pagina:");
        }
    }
}