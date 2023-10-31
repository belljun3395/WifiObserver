package com.wifi.obs.app.domain.service.wifi.iptime;

import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.obs.client.wifi.client.iptime.IptimeHealthClient;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
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

		Optional<HttpStatusResponse> response =
				iptimeHealthClient.query(WifiHostRequest.builder().host(host).build()).getResponse();

		if (response.isEmpty()) {
			return HttpStatus.BAD_REQUEST;
		}
		return response.get().getHttpResponse();
	}
}
