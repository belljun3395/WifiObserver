package com.wifi.obs.client.wifi.client.function;

import java.util.List;

@FunctionalInterface
public interface QueriesAsyncAble<T, R> {

	List<R> queriesAsync(T t);
}
