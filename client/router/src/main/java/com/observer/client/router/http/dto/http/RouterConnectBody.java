package com.observer.client.router.http.dto.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouterConnectBody<T> {

	private final T body;
}
