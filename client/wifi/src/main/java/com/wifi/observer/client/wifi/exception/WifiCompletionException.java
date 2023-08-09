package com.wifi.observer.client.wifi.exception;

import java.util.concurrent.CompletionException;

/** 와이파이 비동기 작업 완료 예외 */
public class WifiCompletionException extends CompletionException {

	public WifiCompletionException(String message) {
		super(message);
	}
}
