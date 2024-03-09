package com.observer.domain.dto.device;

import com.observer.data.entity.device.DeviceEntity;
import java.util.List;
import java.util.stream.Collectors;
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
@Builder(toBuilder = true)
public class GetDevicesUseCaseResponse {

	private List<Device> devices;

	public static GetDevicesUseCaseResponse from(List<DeviceEntity> deviceEntities) {
		List<Device> deviceSources =
				deviceEntities.stream()
						.map(
								deviceEntity ->
										Device.builder()
												.id(deviceEntity.getId())
												.type(deviceEntity.getType().getType())
												.mac(deviceEntity.getMac())
												.info(deviceEntity.getInfo())
												.build())
						.collect(Collectors.toList());
		return new GetDevicesUseCaseResponse(deviceSources);
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	private static class Device {
		private Long id;
		private String type;
		private String mac;
		private String info;
	}
}
