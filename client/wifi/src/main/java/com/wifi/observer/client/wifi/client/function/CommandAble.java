package com.wifi.observer.client.wifi.client.function;

@FunctionalInterface
public interface CommandAble<T, R> {

	R command(T t);
}
