package com.wifi.obs.app.domain.service.wifi.iptime;

import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.observer.client.wifi.client.iptime.IptimeHealthClient;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetIptimeHealthService implements GetHealthService {

	private final IptimeHealthClient iptimeHealthClient;

	public HttpStatus execute(String host) {

		Optional<HttpStatus> response =
				iptimeHealthClient
						.query(CommonWifiHealthRequest.builder().host(host).build())
						.getResponse();

		return response.orElse(HttpStatus.BAD_REQUEST);
	}
}
