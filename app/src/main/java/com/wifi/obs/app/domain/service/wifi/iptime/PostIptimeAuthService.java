package com.wifi.obs.app.domain.service.wifi.iptime;

import com.wifi.observer.client.wifi.client.iptime.IptimeAuthClientImpl;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostIptimeAuthService {

	private final IptimeAuthClientImpl iptimeAuthClient;

	public Optional<AuthInfo> execute(String host, String userName, String password) {
		return iptimeAuthClient
				.command(
						IptimeAuthRequest.builder().host(host).userName(userName).password(password).build())
				.getResponse();
	}
}
