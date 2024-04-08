package com.observer.batch.job.browse.iptime.service;

import com.observer.batch.job.browse.iptime.dao.ConnectHistoryDao;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.support.RecordMapper;
import com.observer.data.support.RecordParser;
import com.observer.data.support.RecordSupportInfo;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveNewRecordService {

	private final ConnectHistoryDao connectHistoryDao;

	private final RecordParser recordParser;
	private final RecordMapper recordMapper;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public ConnectHistoryEntity execute(Long newConnectDeviceId, Long routerId, LocalDateTime now) {
		Optional<ConnectHistoryEntity> disconnectHistoryEntitySource =
				connectHistoryDao.findByDeviceIdAndConnectStatusAndDeletedFalse(
						newConnectDeviceId, ConnectStatus.DISCONNECTED);
		// 이전 연결 기록 및 해지 기록이 없는 경우: 신규 기록 생성
		if (disconnectHistoryEntitySource.isEmpty()) {
			RecordSupportInfo recordInfo = RecordSupportInfo.builder().build();
			return connectHistoryDao.save(
					ConnectHistoryEntity.builder()
							.deviceId(newConnectDeviceId)
							.routerId(routerId)
							.connectTime(now)
							.checkTime(now)
							.disConnectTime(now)
							.record(recordMapper.execute(recordInfo))
							.connectStatus(ConnectStatus.CONNECTED)
							.build());
		}
		// 해지 기록이 있는 경우: 재접속 기록 생성
		ConnectHistoryEntity disConnectHistoryEntity = disconnectHistoryEntitySource.get();
		LocalDateTime disConnectTime = disConnectHistoryEntity.getDisConnectTime();
		String record = disConnectHistoryEntity.getRecord();
		RecordSupportInfo recordInfo = recordParser.execute(record);

		if (disConnectTime.getMonth() != now.getMonth()) {
			recordInfo.resetMonth();
		}
		if (disConnectTime.getDayOfWeek() != now.getDayOfWeek()) {
			recordInfo.resetDay();
		}

		return connectHistoryDao.save(
				ConnectHistoryEntity.builder()
						.deviceId(newConnectDeviceId)
						.routerId(routerId)
						.connectTime(now)
						.checkTime(now)
						.disConnectTime(now)
						.record(recordMapper.execute(recordInfo))
						.connectStatus(ConnectStatus.CONNECTED)
						.build());
	}
}
