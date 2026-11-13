/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service.exception;

public class ConflitoException extends RuntimeException {
    public ConflitoException(String message) {
        super(message);
    }
}
