/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.machine.config;

import com.atacadao.Guardian.machine.action.AvariarProdutoAction;
import com.atacadao.Guardian.machine.action.DevolverProdutoAction;
import com.atacadao.Guardian.machine.action.EntregarProdutoAction;
import com.atacadao.Guardian.machine.action.PerderProdutoAction;
import com.atacadao.Guardian.machine.action.RetirarProdutoAction;
import com.atacadao.Guardian.machine.action.RetornarEstoqueAction;
import com.atacadao.Guardian.machine.action.FinalizarProdutoAction;
import com.atacadao.Guardian.machine.enums.enumEvent.MachineEventos;
import com.atacadao.Guardian.machine.enums.enumState.MachineEstados;
import com.atacadao.Guardian.machine.guard.avariarGuard.AvariarProdutoGuard;
import com.atacadao.Guardian.machine.guard.devolverProduto.DevolverProdutoGuard;
import com.atacadao.Guardian.machine.guard.entregaGuard.EntregarProdutoGuard;
import com.atacadao.Guardian.machine.guard.perdaGuard.PerderProdutoGuard;
import com.atacadao.Guardian.machine.guard.retiradaGuard.RetirarProdutoGuard;
import com.atacadao.Guardian.machine.guard.finalizarProduto.FinalizarProdutoGuard;

import com.atacadao.Guardian.machine.guard.retornarProdutoGuard.RetornarEstoqueGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory
public class StateMachineConfig
        extends EnumStateMachineConfigurerAdapter<MachineEstados, MachineEventos> {

    private final EntregarProdutoGuard entregarProdutoGuard;
    private final PerderProdutoGuard perderProdutoGuard;
    private final RetirarProdutoGuard retirarProdutoGuard;
    private final AvariarProdutoGuard avariarProdutoGuard;
    private final DevolverProdutoGuard devolverProdutoGuard;
    private final RetornarEstoqueGuard retornarEstoqueGuard;
    private final FinalizarProdutoGuard finalizarProdutoGuard;

    private final EntregarProdutoAction entregarProdutoAction;
    private final PerderProdutoAction perderProdutoAction;
    private final RetirarProdutoAction retirarProdutoAction;
    private final AvariarProdutoAction avariarProdutoAction;
    private final DevolverProdutoAction devolverProdutoAction;
    private final RetornarEstoqueAction retornarEstoqueAction;
    private final FinalizarProdutoAction finalizarProdutoAction;

    @Override
    public void configure(
            StateMachineStateConfigurer<MachineEstados, MachineEventos> states
    ) throws Exception {

        states
                .withStates()
                .initial(MachineEstados.ESTOQUE)
                .states(EnumSet.allOf(MachineEstados.class))
                .end(MachineEstados.FINALIZADO)
                .end(MachineEstados.PERDIDO);
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<MachineEstados, MachineEventos> transition
    ) throws Exception {

        transition
                // Retirada
                .withExternal()
                .source(MachineEstados.ESTOQUE)
                .target(MachineEstados.RETIRADO)
                .event(MachineEventos.RETIRAR)
                .guard(retirarProdutoGuard)
                .action(retirarProdutoAction)
                .and()

                // Entrega
                .withExternal()
                .source(MachineEstados.RETIRADO)
                .target(MachineEstados.ENTREGUE)
                .event(MachineEventos.ENTREGAR)
                .guard(entregarProdutoGuard)
                .action(entregarProdutoAction)
                .and()

                // Perda após entrega
                .withExternal()
                .source(MachineEstados.ENTREGUE)
                .target(MachineEstados.PERDIDO)
                .event(MachineEventos.PERDER)
                .guard(perderProdutoGuard)
                .action(perderProdutoAction)
                .and()

                // Avaria após entrega
                .withExternal()
                .source(MachineEstados.ENTREGUE)
                .target(MachineEstados.AVARIADO)
                .event(MachineEventos.AVARIAR)
                .guard(avariarProdutoGuard)
                .action(avariarProdutoAction)
                .and()

                // Avaria após retirada
                .withExternal()
                .source(MachineEstados.RETIRADO)
                .target(MachineEstados.AVARIADO)
                .event(MachineEventos.AVARIAR)
                .guard(avariarProdutoGuard)
                .action(avariarProdutoAction)
                .and()

                // Avaria no estoque
                .withExternal()
                .source(MachineEstados.ESTOQUE)
                .target(MachineEstados.AVARIADO)
                .event(MachineEventos.AVARIAR)
                .guard(avariarProdutoGuard)
                .action(avariarProdutoAction)
                .and()

                // Devolução após retirada
                .withExternal()
                .source(MachineEstados.RETIRADO)
                .target(MachineEstados.DEVOLVIDO)
                .event(MachineEventos.DEVOLVER)
                .guard(devolverProdutoGuard)
                .action(devolverProdutoAction)
                .and()

                // Devolução após entrega
                .withExternal()
                .source(MachineEstados.ENTREGUE)
                .target(MachineEstados.DEVOLVIDO)
                .event(MachineEventos.DEVOLVER)
                .guard(devolverProdutoGuard)
                .action(devolverProdutoAction)
                .and()

                // Retorno ao estoque
                .withExternal()
                .source(MachineEstados.DEVOLVIDO)
                .target(MachineEstados.ESTOQUE)
                .event(MachineEventos.RETORNAR_ESTOQUE)
                .guard(retornarEstoqueGuard)
                .action(retornarEstoqueAction)
                .and()

                // Finalização
                .withExternal()
                .source(MachineEstados.ENTREGUE)
                .target(MachineEstados.FINALIZADO)
                .event(MachineEventos.FINALIZAR)
                .guard(finalizarProdutoGuard)
                .action(finalizarProdutoAction);
    }
}