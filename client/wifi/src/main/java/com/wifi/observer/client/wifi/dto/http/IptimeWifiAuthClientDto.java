package com.wifi.observer.client.wifi.dto.http;

import com.wifi.observer.client.wifi.support.log.HostLogAble;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class IptimeWifiAuthClientDto implements HostLogAble {

	private final String url;
	private final Map<String, String> headers;
	private final Map<String, String> body;

	public String getHost() {
		String sub = StringUtils.substringAfter(url, "http://");
		return StringUtils.substringBefore(sub, "/");
	}
}
