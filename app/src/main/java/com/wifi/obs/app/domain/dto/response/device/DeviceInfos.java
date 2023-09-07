package com.wifi.obs.app.domain.dto.response.device;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = PRIVATE)
public class DeviceInfos {

	private final List<? extends DeviceInfo> devices;

	public static DeviceInfos of(List<? extends DeviceInfo> devices) {
		return new DeviceInfos(devices);
	}
}
