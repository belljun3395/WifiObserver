package com.wifi.obs.client.wifi.client.function;

@FunctionalInterface
public interface QueryAsyncAble<T, R> {

	R queryAsync(T t);
}
