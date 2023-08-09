package com.wifi.observer.client.wifi.dto.response.iptime;

import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
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
public class IptimeAuthResponse implements ClientResponse<AuthInfo> {

	private final AuthInfo response;
	private final String host;

	public Optional<AuthInfo> getResponse() {
		return Optional.ofNullable(response);
	}

	public String getHost() {
		return getResponse().isPresent() ? getResponse().get().getHost() : host;
	}

	public static IptimeAuthResponse fail(String host) {
		return IptimeAuthResponse.builder().host(host).build();
	}
}
