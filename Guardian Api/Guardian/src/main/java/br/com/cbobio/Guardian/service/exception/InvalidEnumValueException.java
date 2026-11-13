/*
 * Copyright (c) 2026, Atacadão S.A. and/or its affiliates. All rights reserved.
 */

package br.com.cbobio.Guardian.service.exception;

import lombok.Getter;

public class InvalidEnumValueException extends RuntimeException {
	
	@Getter
	private final String sourceEnum;
	
	public InvalidEnumValueException(String message, String sourceEnum) {
		super(message);
		this.sourceEnum = sourceEnum;
	}
}
