package com.observer.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenInvalidException extends AuthenticationException {

	public AccessTokenInvalidException(String msg) {
		super(msg);
	}
}
