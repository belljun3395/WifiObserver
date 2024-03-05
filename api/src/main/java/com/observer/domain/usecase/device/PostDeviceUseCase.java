package com.observer.domain.usecase.device;

import com.observer.domain.dto.device.PostDeviceUseCaseRequest;
import com.observer.domain.dto.device.PostDeviceUseCaseResponse;
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
public class PostDeviceUseCase {

	private final DeviceDao deviceDao;

	private final DeviceGetMemberIdService getMemberIdService;
	private final GetMemberRouterIdService getMemberRouterIdService;

	public PostDeviceUseCaseResponse execute(PostDeviceUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouteId();
		final String mac = request.getMac();
		final String info = request.getInfo();

		Long memberId = getMemberIdService.execute(apiKey);

		Optional<Long> routerIdSource = getMemberRouterIdService.execute(routerId, memberId);
		if (routerIdSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Long routerEntityId = routerIdSource.get();

		DeviceEntity deviceEntity =
				deviceDao.saveDevice(
						DeviceEntity.builder()
								.mac(mac)
								.info(info)
								.routerId(routerEntityId)
								.memberId(memberId)
								.build());

		return PostDeviceUseCaseResponse.builder()
				.deviceId(deviceEntity.getId())
				.routeId(deviceEntity.getRouterId())
				.mac(deviceEntity.getMac())
				.info(deviceEntity.getInfo())
				.build();
	}
}
