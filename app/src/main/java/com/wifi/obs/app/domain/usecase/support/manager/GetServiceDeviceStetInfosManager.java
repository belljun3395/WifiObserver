package com.wifi.obs.app.domain.usecase.support.manager;

import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfos;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.web.dto.request.StetType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetServiceDeviceStetInfosManager {

	private final Map<String, GetServiceDeviceStetInfos> getServiceDeviceStetInfosMap;

	public String getKey(StetType type) {
		return getServiceDeviceStetInfosMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public GetServiceDeviceStetInfos getService(String key) {
		return getServiceDeviceStetInfosMap.get(key);
	}

	public GetServiceDeviceStetInfos getService(StetType type) {
		return getServiceDeviceStetInfosMap.get(getKey(type));
	}
}
