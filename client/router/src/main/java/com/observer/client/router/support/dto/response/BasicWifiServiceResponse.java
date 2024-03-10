package com.observer.client.router.support.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BasicWifiServiceResponse {

	private final String host;
}
