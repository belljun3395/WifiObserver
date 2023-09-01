package com.wifi.obs.app.domain.dto.response.device;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class DeviceInfos {

	private final List<? extends DeviceInfo> devices;
}
