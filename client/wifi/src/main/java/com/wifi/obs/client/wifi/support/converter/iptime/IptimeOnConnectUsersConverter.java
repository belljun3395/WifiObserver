package com.wifi.obs.client.wifi.support.converter.iptime;

import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfo;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class IptimeOnConnectUsersConverter {

	public IptimeOnConnectUserInfosResponse from(List<String> infos, String host) {
		List<OnConnectUserInfo> users =
				infos.stream()
						.map(info -> OnConnectUserInfo.builder().user(info).build())
						.collect(Collectors.toList());
		return IptimeOnConnectUserInfosResponse.builder()
				.host(host)
				.response(OnConnectUserInfos.builder().users(users).build())
				.build();
	}
}
