package com.observer.domain.usecase.device;

import com.observer.domain.dto.device.GetDevicesUseCaseRequest;
import com.observer.domain.dto.device.GetDevicesUseCaseResponse;
import com.observer.domain.external.dao.device.DeviceDao;
import com.observer.domain.service.device.DeviceGetMemberIdService;
import com.observer.domain.service.device.GetMemberRouterIdService;
import com.observer.entity.device.DeviceEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDevicesUseCase {

	private final DeviceDao deviceDao;

	private final DeviceGetMemberIdService getMemberIdService;
	private final GetMemberRouterIdService getMemberRouterIdService;

	public GetDevicesUseCaseResponse execute(GetDevicesUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouteId();

		Long memberId = getMemberIdService.execute(apiKey);

		Optional<Long> routerIdSource = getMemberRouterIdService.execute(routerId, memberId);
		if (routerIdSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Long routerEntityId = routerIdSource.get();

		List<DeviceEntity> deviceEntities = deviceDao.findAllByRouterIdAndDeletedFalse(routerEntityId);
		List<DeviceEntity> memberDeviceEntities =
				deviceEntities.stream()
						.filter(entity -> !entity.getMemberId().equals(memberId))
						.collect(Collectors.toList());

		return GetDevicesUseCaseResponse.from(memberDeviceEntities);
	}
}
