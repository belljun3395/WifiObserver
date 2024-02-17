package com.observer.client.router.http.dto.http;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class RouterConnectStatus {

	private final HttpStatus status;
}
