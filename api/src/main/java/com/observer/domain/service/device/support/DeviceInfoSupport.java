package com.observer.domain.service.device.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeviceInfoSupport {

	private Long deviceId;
	private Long routerId;
	private Long memberId;
	private String type;
	private String mac;
	private String info;
}
