/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service.exception;

public class Erro500Exception extends RuntimeException {

    public Erro500Exception(String message) {
        super(message);
    }
}
