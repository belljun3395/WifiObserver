package com.wifi.obs.client.wifi.dto.response;

import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class HealthStatusResponse implements ClientResponse<HttpStatusResponse> {

	private final HttpStatusResponse response;
	private final String host;

	@Override
	public Optional<HttpStatusResponse> getResponse() {
		return Optional.ofNullable(response);
	}
}
