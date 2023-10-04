package com.wifi.obs.app.domain.dto.response.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ServiceDeviceStetInfos {

	@JsonProperty(value = "serviceId")
	private final Long id;

	private final List<? extends DeviceStetInfo> stets;
}
