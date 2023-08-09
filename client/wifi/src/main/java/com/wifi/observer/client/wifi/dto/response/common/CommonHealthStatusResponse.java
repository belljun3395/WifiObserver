package com.wifi.observer.client.wifi.dto.response.common;

import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommonHealthStatusResponse implements ClientResponse<HttpStatus> {

	private final HttpStatus response;
	private final String host;

	public Optional<HttpStatus> getResponse() {
		return Optional.ofNullable(response);
	}
}
