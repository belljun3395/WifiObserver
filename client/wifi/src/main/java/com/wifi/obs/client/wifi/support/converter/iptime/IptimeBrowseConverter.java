package com.wifi.obs.client.wifi.support.converter.iptime;

import com.wifi.obs.client.wifi.dto.http.iptime.IptimeWifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfo;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
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
						.map(source -> OnConnectUserInfo.builder().user(source).build())
						.collect(Collectors.toList());

		if (users.isEmpty()) {
			return IptimeOnConnectUserInfosResponse.fail(host);
		}

		return IptimeOnConnectUserInfosResponse.builder()
				.host(host)
				.response(new OnConnectUserInfos(users))
				.build();
	}

	public IptimeWifiBrowseRequestElement to(
			WifiBrowseRequest browseRequest, Map<String, String> headers, String cookie) {
		return IptimeWifiBrowseRequestElement.builder()
				.url(HTTP + browseRequest.getHost() + loginTimeproQuery)
				.headers(headers)
				.cookie(cookie)
				.build();
	}
}
