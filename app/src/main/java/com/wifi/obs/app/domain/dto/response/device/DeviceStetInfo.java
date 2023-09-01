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
public class DeviceStetInfo {
	private Long id;
	private String mac;
	@Builder.Default private Long time = 0L;
}
