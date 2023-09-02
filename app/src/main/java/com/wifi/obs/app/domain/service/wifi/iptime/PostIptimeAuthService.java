package com.wifi.obs.app.domain.service.wifi.iptime;

import com.wifi.observer.client.wifi.client.iptime.IptimeAuthClientImpl;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostIptimeAuthService {

	private final IptimeAuthClientImpl iptimeAuthClient;

	public AuthInfo execute(String host, String userName, String password) {
		return iptimeAuthClient
				.command(
						IptimeAuthRequest.builder().host(host).userName(userName).password(password).build())
				.getResponse()
				.orElseThrow(() -> new RuntimeException("인증에 실패했습니다."));
	}
}
