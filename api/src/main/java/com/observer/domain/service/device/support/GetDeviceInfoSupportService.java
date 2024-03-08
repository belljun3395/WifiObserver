package com.observer.domain.service.device.support;

import com.observer.domain.external.dao.device.DeviceDao;
import com.observer.entity.device.DeviceEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDeviceInfoSupportService {

	private final DeviceDao deviceDao;

	public Optional<DeviceInfoSupport> execute(Long deviceId) {
		Optional<DeviceEntity> deviceEntitySource = deviceDao.findByIdAndDeletedFalse(deviceId);
		if (deviceEntitySource.isEmpty()) {
			return Optional.empty();
		}
		DeviceEntity deviceEntity = deviceEntitySource.get();
		return Optional.of(
				DeviceInfoSupport.builder()
						.deviceId(deviceEntity.getId())
						.routerId(deviceEntity.getRouterId())
						.memberId(deviceEntity.getMemberId())
						.type(deviceEntity.getType().getType())
						.mac(deviceEntity.getMac())
						.info(deviceEntity.getInfo())
						.build());
	}
}
