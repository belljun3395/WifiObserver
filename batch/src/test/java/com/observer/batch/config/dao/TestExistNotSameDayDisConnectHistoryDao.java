package com.observer.batch.config.dao;

import com.observer.batch.job.browse.iptime.dao.ConnectHistoryDao;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.support.RecordMapper;
import com.observer.data.support.RecordSupportInfo;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class TestExistNotSameDayDisConnectHistoryDao implements ConnectHistoryDao {

	public static LocalDateTime CONNECT_TIME = LocalDateTime.of(2024, 3, 1, 23, 30);
	public static LocalDateTime CHECK_TIME = CONNECT_TIME.plusMinutes(10L);
	public static LocalDateTime DISCONNECT_TIME = CONNECT_TIME.plusMinutes(20L);

	public static Long ACCUMULATE_MINUTES = 20L;

	private final RecordMapper recordMapper;

	@Override
	public ConnectHistoryEntity save(ConnectHistoryEntity entity) {
		return entity;
	}

	@Override
	public Optional<ConnectHistoryEntity> findByDeviceIdAndConnectStatusAndDeletedFalse(
			Long deviceId, ConnectStatus connectStatus) {
		RecordSupportInfo record =
				RecordSupportInfo.builder().month(ACCUMULATE_MINUTES).day(ACCUMULATE_MINUTES).build();
		ConnectHistoryEntity historyEntity =
				ConnectHistoryEntity.builder()
						.id(1L)
						.deviceId(1L)
						.routerId(1L)
						.connectTime(CONNECT_TIME)
						.checkTime(CHECK_TIME)
						.disConnectTime(DISCONNECT_TIME)
						.connectStatus(ConnectStatus.DISCONNECTED)
						.record(recordMapper.execute(record))
						.build();
		return Optional.of(historyEntity);
	}
}
