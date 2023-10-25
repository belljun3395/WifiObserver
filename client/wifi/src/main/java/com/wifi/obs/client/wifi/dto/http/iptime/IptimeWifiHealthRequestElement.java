package com.wifi.obs.client.wifi.dto.http.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
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
public class IptimeWifiHealthRequestElement implements WifiHealthRequestElement {

	private final String url;

	@Override
	public String getHost() {
		String sub = StringUtils.substringAfter(url, "http://");
		return StringUtils.substringBefore(sub, "/");
	}
}
