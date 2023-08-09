package com.wifi.observer.client.wifi.dto.http;

import com.wifi.observer.client.wifi.support.log.HostLogAble;
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
public class IptimeWifiHealthClientDto implements HostLogAble {
	private final String host;

	public String getURL() {
		return "http://" + host;
	}
}
