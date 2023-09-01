package com.wifi.obs.app.domain.dto.response.service;

import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import java.util.List;
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
public class WifiServiceInfo {

	private Long id;
	private WifiServiceType type;
	private Long cycle;
	private WifiStatus status;
	private String createAt;
	private List<DeviceInfo> deviceInfos;
}
