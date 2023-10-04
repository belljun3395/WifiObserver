package com.wifi.obs.app.domain.usecase.support.manager;

import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
import com.wifi.obs.app.domain.service.wifi.GetUserService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserServiceManager {

	private final Map<String, GetUserService> getUsersServiceMap;

	public String getKey(WifiServiceType type) {
		return getUsersServiceMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public GetUserService getService(String key) {
		return getUsersServiceMap.get(key);
	}

	public GetUserService getService(WifiServiceType type) {
		return getUsersServiceMap.get(getKey(type));
	}
}
