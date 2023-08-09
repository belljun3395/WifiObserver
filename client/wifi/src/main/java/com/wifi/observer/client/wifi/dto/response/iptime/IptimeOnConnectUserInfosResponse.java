package com.wifi.observer.client.wifi.dto.response.iptime;

import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
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
public class IptimeOnConnectUserInfosResponse implements ClientResponse<OnConnectUserInfos> {

	private final String host;
	private final OnConnectUserInfos response;

	public Optional<OnConnectUserInfos> getResponse() {
		return Optional.ofNullable(response);
	}

	public static IptimeOnConnectUserInfosResponse fail(String host) {
		return IptimeOnConnectUserInfosResponse.builder().host(host).build();
	}
}
