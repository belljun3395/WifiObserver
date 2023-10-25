package com.wifi.obs.client.wifi.client.function;

import java.util.List;

@FunctionalInterface
public interface QueriesAble<T, R> {

	List<R> queries(T t);
}
