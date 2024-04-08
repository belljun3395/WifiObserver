package com.observer.client.router.service.iptime;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.http.client.HealthClient;
import com.observer.client.router.http.dto.http.RouterConnectStatus;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiHealthClientDto;
import com.observer.client.router.service.RouterHealthService;
import com.observer.client.router.support.dto.request.WifiHealthServiceRequest;
import com.observer.client.router.support.dto.response.RouterHealthResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeHealthService implements RouterHealthService {

	private static final String HTTP = "http://";

	private final HealthClient<IptimeWifiHealthClientDto> healthClient;

	@Override
	public RouterHealthResponse execute(WifiHealthServiceRequest request) throws ClientException {
		final String host = request.getHost();
		IptimeWifiHealthClientDto dto = getClientDto(request);

		RouterConnectStatus clientResponse = null;
		try {
			clientResponse = healthClient.execute(dto);
		} catch (IOException e) {
			throw new ClientException(e);
		}
		RouterConnectStatus health = Objects.requireNonNull(clientResponse);

		return RouterHealthResponse.builder().host(host).response(health.getStatus()).build();
	}

	private IptimeWifiHealthClientDto getClientDto(WifiHealthServiceRequest request) {
		return IptimeWifiHealthClientDto.builder().url(HTTP + request.getHost()).build();
	}
}
