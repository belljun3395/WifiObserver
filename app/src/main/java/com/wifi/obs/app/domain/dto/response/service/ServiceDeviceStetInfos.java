package com.wifi.obs.app.domain.dto.response.service;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ServiceDeviceStetInfos {

	private final Long serviceId;
	private final List<? extends DeviceStetInfo> stetInfos;
}
