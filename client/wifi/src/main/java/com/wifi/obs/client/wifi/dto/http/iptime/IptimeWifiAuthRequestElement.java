package com.wifi.obs.client.wifi.dto.http.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
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
public class IptimeWifiAuthRequestElement implements WifiAuthRequestElement {

	private final String url;
	private final Map<String, String> headers;
	private final Map<String, String> body;

	@Override
	public String getHost() {
		String sub = StringUtils.substringAfter(url, "http://");
		return StringUtils.substringBefore(sub, "/");
	}
}
