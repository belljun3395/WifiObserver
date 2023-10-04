package com.wifi.obs.app.domain.dto.response.device;

import com.wifi.obs.app.domain.model.device.DeviceType;
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
public class DeviceInfo {
	private Long id;
	private DeviceType type;
	private String mac;
}
