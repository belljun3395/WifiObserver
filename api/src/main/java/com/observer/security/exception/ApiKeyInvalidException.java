package com.observer.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ApiKeyInvalidException extends AuthenticationException {

	public ApiKeyInvalidException(String msg) {
		super(msg);
	}
}
