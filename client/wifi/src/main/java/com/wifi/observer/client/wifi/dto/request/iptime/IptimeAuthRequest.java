package com.wifi.observer.client.wifi.dto.request.iptime;

import com.wifi.observer.client.wifi.dto.request.WifiAuthClientRequest;
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
public class IptimeAuthRequest implements WifiAuthClientRequest {
	private final String host;
	private final String userName;
	private final String password;
}
