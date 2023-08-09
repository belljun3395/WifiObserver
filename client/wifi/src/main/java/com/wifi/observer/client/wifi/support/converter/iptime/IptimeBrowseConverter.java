package com.wifi.observer.client.wifi.support.converter.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfo;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IptimeBrowseConverter {
	private static final String HTTP = "http://";

	@Value("${iptime.http.login.timepro.query}")
	private String loginTimeproQuery;

	public IptimeOnConnectUserInfosResponse from(List<String> sources, String host) {
		List<OnConnectUserInfo> users =
				sources.stream()
						.map(source -> OnConnectUserInfo.builder().user(source).host(host).build())
						.collect(Collectors.toList());

		if (users.isEmpty()) {
			return IptimeOnConnectUserInfosResponse.fail(host);
		}

		return IptimeOnConnectUserInfosResponse.builder()
				.host(host)
				.response(new OnConnectUserInfos(users, host))
				.build();
	}

	public IptimeWifiBrowseClientDto to(
			WifiBrowseRequest browseRequest, Map<String, String> headers, String cookie) {
		return IptimeWifiBrowseClientDto.builder()
				.url(HTTP + browseRequest.getHost() + loginTimeproQuery)
				.headers(headers)
				.cookie(cookie)
				.build();
	}
}
