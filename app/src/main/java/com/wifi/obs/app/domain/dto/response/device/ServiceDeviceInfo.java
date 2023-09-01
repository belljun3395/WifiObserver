package com.wifi.obs.app.domain.dto.response.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ServiceDeviceInfo extends DeviceInfo {

	private Long serviceId;
}
