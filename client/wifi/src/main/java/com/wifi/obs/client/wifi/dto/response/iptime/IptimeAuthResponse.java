package com.wifi.obs.client.wifi.dto.response.iptime;

import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
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

	@Override
	public Optional<AuthInfo> getResponse() {
		return Optional.ofNullable(response);
	}

	public static IptimeAuthResponse fail(String host) {
		return IptimeAuthResponse.builder().host(host).build();
	}
}
