package com.observer.domain.service.connection;

import com.observer.domain.service.connection.dto.ConnectionDeviceInfo;
import com.observer.domain.service.device.support.DeviceInfoSupport;
import com.observer.domain.service.device.support.GetDeviceInfoSupportQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetConnectionDeviceInfoQuery {

	private final GetDeviceInfoSupportQuery getDeviceInfoSupportQuery;

	public Optional<ConnectionDeviceInfo> execute(Long deviceId) {
		Optional<DeviceInfoSupport> deviceInfoSupportSource =
				getDeviceInfoSupportQuery.execute(deviceId);
		if (deviceInfoSupportSource.isEmpty()) {
			return Optional.empty();
		}
		DeviceInfoSupport deviceInfoSupport = deviceInfoSupportSource.get();
		return Optional.of(
				ConnectionDeviceInfo.builder()
						.deviceId(deviceInfoSupport.getDeviceId())
						.routerId(deviceInfoSupport.getRouterId())
						.memberId(deviceInfoSupport.getMemberId())
						.type(deviceInfoSupport.getType())
						.mac(deviceInfoSupport.getMac())
						.info(deviceInfoSupport.getInfo())
						.build());
	}
}
