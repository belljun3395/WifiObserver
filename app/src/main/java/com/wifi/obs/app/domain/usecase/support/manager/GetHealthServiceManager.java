package com.wifi.obs.app.domain.usecase.support.manager;

import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.web.dto.request.service.ServiceType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetHealthServiceManager {

	private final Map<String, GetHealthService> getHealthServiceMap;

	public String getKey(ServiceType type) {
		return getHealthServiceMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public String getKey(WifiServiceType type) {
		return getHealthServiceMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public GetHealthService getService(String key) {
		return getHealthServiceMap.get(key);
	}

	public GetHealthService getService(ServiceType type) {
		return getHealthServiceMap.get(getKey(type));
	}

	public GetHealthService getService(WifiServiceType type) {
		return getHealthServiceMap.get(getKey(type));
	}
}
