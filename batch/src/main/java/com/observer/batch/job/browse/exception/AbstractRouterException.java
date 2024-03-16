package com.observer.batch.job.browse.exception;

public abstract class AbstractRouterException extends RuntimeException {

	public AbstractRouterException(String request) {
		super("request: " + request);
	}
}
