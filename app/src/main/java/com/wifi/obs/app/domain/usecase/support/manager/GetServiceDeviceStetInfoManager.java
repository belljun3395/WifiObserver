package com.wifi.obs.app.domain.usecase.support.manager;

import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfo;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.web.dto.request.StetType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetServiceDeviceStetInfoManager {

	private final Map<String, GetServiceDeviceStetInfo> getServiceDeviceStetInfoMap;

	public String getKey(StetType type) {
		return getServiceDeviceStetInfoMap.keySet().stream()
				.filter(s -> s.contains(type.getType()))
				.findFirst()
				.orElseThrow(BadTypeRequestException::new);
	}

	public GetServiceDeviceStetInfo getService(String key) {
		return getServiceDeviceStetInfoMap.get(key);
	}

	public GetServiceDeviceStetInfo getService(StetType type) {
		return getServiceDeviceStetInfoMap.get(getKey(type));
	}
}
