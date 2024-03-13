package com.observer.batch.job.browse.iptime.step;

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
import com.observer.data.support.RecordMapper;
import com.observer.data.support.RecordParser;
import com.observer.data.support.RecordSupportInfo;
import java.time.Duration;
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

	private final RecordParser recordParser;
	private final RecordMapper recordMapper;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void write(List<? extends RouterUsersResponse> items) {
		final LocalDateTime now = LocalDateTime.now();

		log.info("===> IptimeConnectHistoryWriter.write() time: {}", now);
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
					log.info("======> saveDisconnectRecord() deviceId: {}", historyEntity.getDeviceId());
					saveDisconnectRecord(historyEntity, now);
					continue;
				}
				// 접속 기록(historyEntity)의 Device Id가 접속 중인 Id(connectDeviceIds) 목록에 있는 경우: 접속이 유지 중인 경우
				log.info("======> checkRecord() deviceId: {}", historyEntity.getDeviceId());
				checkRecord(historyEntity, now);
				ifContainsRemoveDeviceIds.remove(historyEntityDeviceId);
			}

			// 신규 접속 Device Id 목록
			Set<Long> newConnectDeviceIds = ifContainsRemoveDeviceIds;
			log.info("======> newConnectDeviceIds: {}", newConnectDeviceIds);
			for (Long newConnectDeviceId : newConnectDeviceIds) {
				log.info("======> saveNewRecord() deviceId: {}", newConnectDeviceId);
				saveNewRecord(newConnectDeviceId, routerId, now);
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

	private void saveDisconnectRecord(ConnectHistoryEntity historyEntity, LocalDateTime now) {
		final LocalDateTime connectDateTime = historyEntity.getCheckTime();
		final long connectedMinutes = Duration.between(connectDateTime, now).toMinutes();
		final String record = historyEntity.getRecord();

		RecordSupportInfo recordSupportInfo = recordParser.execute(record);
		String newRecord = getNewRecord(now, connectDateTime, recordSupportInfo, connectedMinutes);

		connectHistoryRepository.save(
				historyEntity.toBuilder()
						.disConnectTime(now)
						.connectStatus(ConnectStatus.DISCONNECTED)
						.record(newRecord)
						.build());
	}

	private void checkRecord(ConnectHistoryEntity historyEntity, LocalDateTime now) {
		final LocalDateTime connectDateTime = historyEntity.getCheckTime();
		final long connectedMinutes = Duration.between(connectDateTime, now).toMinutes();
		final String record = historyEntity.getRecord();

		boolean isNewEntity = false;
		RecordSupportInfo recordSupportInfo = recordParser.execute(record);

		// 접속한 시간과 접속이 끊긴 시간 사이에 월이 바뀐 경우: 월 단위로 누적 시간 계산
		if (connectDateTime.getMonth() != now.getMonth()) {
			// 이전 월까지의 누적 시간 계산
			final LocalDateTime nowMonthStartDateTime =
					LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
			LocalDateTime adjustConnectDateTime =
					LocalDateTime.of(
							connectDateTime.getYear(),
							connectDateTime.getMonth(),
							connectDateTime.getDayOfMonth(),
							connectDateTime.getHour(),
							connectDateTime.getMinute());
			long beforeNowAccumulateMinutes =
					Duration.between(adjustConnectDateTime, nowMonthStartDateTime).toMinutes();
			recordSupportInfo.accumulate(beforeNowAccumulateMinutes);
			String beforeNowRecord = recordMapper.execute(recordSupportInfo);
			recordSupportInfo.resetMonth();

			// 이전 월까지의 누적 시간을 기록
			connectHistoryRepository.save(
					historyEntity.toBuilder()
							.disConnectTime(nowMonthStartDateTime)
							.connectStatus(ConnectStatus.DISCONNECTED)
							.record(beforeNowRecord)
							.build());

			isNewEntity = true;
			long remainAccumulateMinutes = connectedMinutes - beforeNowAccumulateMinutes;
			recordSupportInfo.accumulate(remainAccumulateMinutes);
		}

		String newRecord = getNewRecord(now, connectDateTime, recordSupportInfo, connectedMinutes);

		if (isNewEntity) {
			connectHistoryRepository.save(
					ConnectHistoryEntity.builder()
							.deviceId(historyEntity.getDeviceId())
							.routerId(historyEntity.getRouterId())
							.connectTime(now)
							.checkTime(now)
							.connectStatus(ConnectStatus.CONNECTED)
							.record(newRecord)
							.build());
		} else {
			connectHistoryRepository.save(
					historyEntity.toBuilder()
							.checkTime(now)
							.connectStatus(ConnectStatus.CONNECTED)
							.record(newRecord)
							.build());
		}
	}

	private String getNewRecord(
			LocalDateTime now,
			LocalDateTime connectDateTime,
			RecordSupportInfo recordSupportInfo,
			long connectedMinutes) {
		if (connectDateTime.getDayOfWeek() != now.getDayOfWeek()) {
			final LocalDateTime nowStartDateTime =
					LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
			LocalDateTime adjustConnectDateTime =
					LocalDateTime.of(
							connectDateTime.getYear(),
							connectDateTime.getMonth(),
							connectDateTime.getDayOfMonth(),
							connectDateTime.getHour(),
							connectDateTime.getMinute());
			long beforeNowAccumulateMinutes =
					Duration.between(adjustConnectDateTime, nowStartDateTime).toMinutes();
			recordSupportInfo.accumulate(beforeNowAccumulateMinutes);
			recordSupportInfo.resetDay();
			long remainAccumulateMinutes = connectedMinutes - beforeNowAccumulateMinutes;
			recordSupportInfo.accumulate(remainAccumulateMinutes);
		} else {
			recordSupportInfo.accumulate(connectedMinutes);
		}
		return recordMapper.execute(recordSupportInfo);
	}

	private void saveNewRecord(Long newConnectDeviceId, Long routerId, LocalDateTime now) {
		connectHistoryRepository.save(
				ConnectHistoryEntity.builder()
						.deviceId(newConnectDeviceId)
						.routerId(routerId)
						.connectTime(now)
						.checkTime(now)
						.disConnectTime(now)
						.connectStatus(ConnectStatus.CONNECTED)
						.build());
	}
}
