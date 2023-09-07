package com.wifi.obs.app.domain.usecase.support.manager;

import com.wifi.obs.app.domain.service.wifi.PostAuthService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.web.dto.request.service.ServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAuthServiceManager {

	private final Map<String, PostAuthService> postAuthServiceMap;

	public String getKey(WifiServiceType type) {
		return postAuthServiceMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public String getKey(ServiceType type) {
		return postAuthServiceMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public PostAuthService getService(String key) {
		return postAuthServiceMap.get(key);
	}

	public PostAuthService getService(ServiceType type) {
		return postAuthServiceMap.get(getKey(type));
	}
}
