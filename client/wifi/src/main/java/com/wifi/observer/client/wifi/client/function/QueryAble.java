package com.wifi.observer.client.wifi.client.function;

@FunctionalInterface
public interface QueryAble<T, R> {

	R query(T t);
}
