package com.wifi.observer.client.wifi.model.info;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class HealthQueryInfo implements HttpStatusInfo {

	private final HttpStatus info;
}
