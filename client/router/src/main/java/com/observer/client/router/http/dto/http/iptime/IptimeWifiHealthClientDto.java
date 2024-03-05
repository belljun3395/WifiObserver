package com.observer.client.router.http.dto.http.iptime;

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
public class IptimeWifiHealthClientDto {

	private final String url;

	public String getHost() {
		String sub = StringUtils.substringAfter(url, "http://");
		return StringUtils.substringBefore(sub, "/");
	}
}