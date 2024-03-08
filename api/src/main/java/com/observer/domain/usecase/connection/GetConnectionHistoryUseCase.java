package com.observer.domain.usecase.connection;

import com.observer.domain.dto.connection.ConnectionHistoryInfo;
import com.observer.domain.dto.connection.GetConnectionHistoryUseCaseRequest;
import com.observer.domain.dto.connection.GetConnectionHistoryUseCaseResponse;
import com.observer.domain.external.dao.history.connect.ConnectHistoryDao;
import com.observer.domain.service.connection.GetConnectionDeviceInfoService;
import com.observer.domain.service.connection.GetConnectionMemberIdService;
import com.observer.domain.service.connection.GetConnectionMemberRouterHostInfoService;
import com.observer.domain.service.connection.dto.ConnectionDeviceInfo;
import com.observer.domain.service.connection.dto.RouterHostInfo;
import com.observer.entity.history.ConnectHistoryEntity;
import com.observer.persistence.history.connect.ConnectHistoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetConnectionHistoryUseCase {

	private final ConnectHistoryDao connectHistoryRepository;

	private final GetConnectionMemberIdService getConnectionMemberIdService;
	private final GetConnectionMemberRouterHostInfoService getConnectionMemberRouterHostInfoService;

	private final GetConnectionDeviceInfoService getConnectionDeviceInfoService;

	@Transactional(readOnly = true)
	public GetConnectionHistoryUseCaseResponse execute(GetConnectionHistoryUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouterId();

		Long memberId = getConnectionMemberIdService.execute(apiKey);

		Optional<RouterHostInfo> routerHostInfoSource =
				getConnectionMemberRouterHostInfoService.execute(routerId, memberId);
		if (routerHostInfoSource.isEmpty()) {
			throw new IllegalArgumentException("not found router info");
		}
		RouterHostInfo routerHostInfo = routerHostInfoSource.get();

		List<ConnectHistoryEntity> historyEntitySources =
				connectHistoryRepository.findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
						routerHostInfo.getRouterId(), LocalDateTime.now());
		Map<Long, String> deviceRecords =
				historyEntitySources.stream()
						.collect(
								Collectors.toMap(
										ConnectHistoryEntity::getDeviceId, ConnectHistoryEntity::getRecord));

		List<ConnectionHistoryInfo> connectionHistoryInfos = new ArrayList<>();
		for (Map.Entry<Long, String> deviceRecord : deviceRecords.entrySet()) {
			final Long deviceId = deviceRecord.getKey();
			final String record = deviceRecord.getValue();
			Optional<ConnectionDeviceInfo> deviceInfoSource =
					getConnectionDeviceInfoService.execute(deviceId);
			if (deviceInfoSource.isEmpty()) {
				continue;
			}
			ConnectionDeviceInfo deviceInfo = deviceInfoSource.get();
			connectionHistoryInfos.add(
					ConnectionHistoryInfo.builder()
							.deviceId(deviceId)
							.routerId(routerHostInfo.getRouterId())
							.memberId(memberId)
							.type(deviceInfo.getType())
							.mac(deviceInfo.getMac())
							.info(deviceInfo.getInfo())
							.record(record)
							.build());
		}
		return GetConnectionHistoryUseCaseResponse.of(connectionHistoryInfos);
	}
}
