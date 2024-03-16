package com.observer.batch.job.browse.iptime.step;

import com.observer.batch.job.browse.iptime.service.CheckRecordService;
import com.observer.batch.job.browse.iptime.service.SaveDisconnectRecordService;
import com.observer.batch.job.browse.iptime.service.SaveNewRecordService;
import com.observer.client.router.support.dto.response.RouterUser;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.device.DeviceEntity;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.entity.router.RouterEntity;
import com.observer.data.persistence.device.DeviceRepository;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import com.observer.data.persistence.router.RouterRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeConnectHistoryWriter implements ItemWriter<RouterUsersResponse> {

	private final RouterRepository routerRepository;
	private final DeviceRepository deviceRepository;

	private final ConnectHistoryRepository connectHistoryRepository;

	private final SaveDisconnectRecordService saveDisconnectRecordService;
	private final CheckRecordService checkRecordService;
	private final SaveNewRecordService saveNewRecordService;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void write(List<? extends RouterUsersResponse> items) {
		final LocalDateTime now = LocalDateTime.now();

		log.info("======================================================");
		for (RouterUsersResponse item : items) {
			final String host = item.getHost();
			final List<RouterUser> connectUsers = item.getResponse().getUsers();
			log.info("======> host: {}", host);
			log.info("======> connectUsers: {}", connectUsers);

			// RouterEntity 조회
			RouterEntity routerEntity = getRouterEntity(host);
			if (routerEntity == null) {
				log.warn("RouterEntity is not found. host: {}", host);
				continue;
			}
			final Long routerId = routerEntity.getId();

			// ConnectHistoryEntity 조회
			List<ConnectHistoryEntity> historyEntities =
					connectHistoryRepository.findAllByRouterIdAndConnectStatusAndDeletedFalse(
							routerId, ConnectStatus.CONNECTED);

			// DeviceEntity 조회
			List<DeviceEntity> deviceEntities =
					deviceRepository.findAllByRouterIdAndDeletedFalse(routerId);

			// 접속 중인 DeviceEntity Id 필터링
			Set<Long> connectDeviceIds = getConnectDeviceIds(deviceEntities, connectUsers);
			log.info("======> connectDeviceIds: {}", connectDeviceIds);

			// 남아 있는 Device Id: 접속 중이지만 기록이 없는 경우(신규 접속)
			Set<Long> ifContainsRemoveDeviceIds = new HashSet<>(connectDeviceIds);

			for (ConnectHistoryEntity historyEntity : historyEntities) {
				// 접속 기록(historyEntity)의 Device Id가 접속 중인 Id(connectDeviceIds) 목록에 없는 경우: 접속이 끊어진 경우
				final Long historyEntityDeviceId = historyEntity.getDeviceId();
				if (!ifContainsRemoveDeviceIds.contains(historyEntityDeviceId)) {
					saveDisconnectRecordService.execute(historyEntity, now);
					continue;
				}
				// 접속 기록(historyEntity)의 Device Id가 접속 중인 Id(connectDeviceIds) 목록에 있는 경우: 접속이 유지 중인 경우
				checkRecordService.execute(historyEntity, now);
				ifContainsRemoveDeviceIds.remove(historyEntityDeviceId);
			}

			// 신규 접속 Device Id 목록
			Set<Long> newConnectDeviceIds = ifContainsRemoveDeviceIds;
			log.info("======> newConnectDeviceIds: {}", newConnectDeviceIds);
			for (Long newConnectDeviceId : newConnectDeviceIds) {
				saveNewRecordService.execute(newConnectDeviceId, routerId, now);
			}
		}
		log.info("======================================================");
	}

	private RouterEntity getRouterEntity(String host) {
		Optional<RouterEntity> routerEntitySource = routerRepository.findByHostAndDeletedFalse(host);
		if (routerEntitySource.isEmpty()) {
			log.error("RouterEntity is not found. host: {}", host);
			return null;
		}
		return routerEntitySource.get();
	}

	private Set<Long> getConnectDeviceIds(
			List<DeviceEntity> deviceEntities, List<RouterUser> connectUsers) {
		Map<String, Long> deviceMacAndIdInfos =
				deviceEntities.stream()
						.collect(Collectors.toMap(DeviceEntity::getMac, DeviceEntity::getId));
		return connectUsers.stream()
				.map(RouterUser::getUser)
				.filter(s -> Objects.nonNull(deviceMacAndIdInfos.get(s)))
				.map(deviceMacAndIdInfos::get)
				.collect(Collectors.toSet());
	}
}
