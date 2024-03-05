package com.observer.domain.usecase.device;

import com.observer.domain.dto.device.DeleteDeviceUseCaseRequest;
import com.observer.domain.external.dao.device.DeviceDao;
import com.observer.domain.service.device.DeviceGetMemberIdService;
import com.observer.domain.service.device.GetMemberRouterIdService;
import com.observer.entity.device.DeviceEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteDeviceUseCase {
	private final DeviceDao deviceDao;

	private final DeviceGetMemberIdService getMemberIdService;
	private final GetMemberRouterIdService getMemberRouterIdService;

	public void execute(DeleteDeviceUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouteId();
		final Long deviceId = request.getDeviceId();

		Long memberId = getMemberIdService.execute(apiKey);

		Optional<Long> routerIdSource = getMemberRouterIdService.execute(routerId, memberId);
		if (routerIdSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Long routerEntityId = routerIdSource.get();

		Optional<DeviceEntity> deviceEntitySource = deviceDao.findByIdAndDeletedFalse(deviceId);
		if (deviceEntitySource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		DeviceEntity deviceEntity = deviceEntitySource.get();

		if (!deviceEntity.getMemberId().equals(memberId)) {
			throw new IllegalArgumentException();
		}

		if (!deviceEntity.getRouterId().equals(routerEntityId)) {
			throw new IllegalArgumentException();
		}

		deviceDao.deleteDevice(deviceEntity);
	}
}
