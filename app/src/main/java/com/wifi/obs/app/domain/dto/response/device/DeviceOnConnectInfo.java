package com.wifi.obs.app.domain.dto.response.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeviceOnConnectInfo {
	private Long id;
	private String mac;
	private Boolean connected;
}
