package com.observer.domain.dto.device;

import com.observer.data.entity.device.DeviceType;
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
public class PostDeviceUseCaseResponse {

	private Long deviceId;
	private Long routeId;
	@Builder.Default private String type = DeviceType.NOTEBOOK.getType();
	private String mac;
	private String info;
}
