package com.observer.domain.dto.device;

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
public class DeleteDeviceUseCaseRequest {

	private String apiKey;
	private Long routeId;
	private Long deviceId;
}
