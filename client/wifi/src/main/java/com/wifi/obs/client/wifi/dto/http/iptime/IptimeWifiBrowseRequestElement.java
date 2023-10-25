package com.wifi.obs.client.wifi.dto.http.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
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
public class IptimeWifiBrowseRequestElement implements WifiBrowseRequestElement {

	private final String url;
	private final Map<String, String> headers;
	private final String cookie;

	@Override
	public String getHost() {
		String sub = StringUtils.substringAfter(url, "http://");
		return StringUtils.substringBefore(sub, "/");
	}
}
