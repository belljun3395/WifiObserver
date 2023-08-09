package com.wifi.observer.client.wifi.client.function;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface QueriesAsyncAble<T, R> {

	List<R> queriesAsync(T t);

	List<CompletableFuture<R>> getFutures(T t);
}
