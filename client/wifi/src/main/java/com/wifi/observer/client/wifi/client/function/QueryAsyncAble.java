package com.wifi.observer.client.wifi.client.function;

import java.util.concurrent.CompletableFuture;

public interface QueryAsyncAble<T, R> {

	R queryAsync(T t);

	CompletableFuture<R> getFuture(T t);
}
