package com.wifi.obs.client.wifi.model;

import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Health {

	private final HttpStatusResponse status;
	private final String host;

	public static Health fail(String host) {
		return Health.builder()
				.status(HttpStatusResponse.of(HttpStatus.BAD_REQUEST))
				.host(host)
				.build();
	}

	public boolean isFail() {
		return Objects.equals(status, HttpStatusResponse.of(HttpStatus.BAD_REQUEST));
	}

	public HttpStatusResponse getHttpStatus() {
		return status;
	}
}
