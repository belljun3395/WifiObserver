package com.wifi.obs.client.wifi.dto.request.iptime;

import com.wifi.obs.client.wifi.dto.request.WifiAuthRequest;
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
public class IptimeAuthRequest implements WifiAuthRequest {

	private final String host;
	private final String userName;
	private final String password;
}
