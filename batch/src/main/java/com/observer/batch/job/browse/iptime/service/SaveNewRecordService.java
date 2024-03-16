package com.observer.batch.job.browse.iptime.service;

import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveNewRecordService {

	private final ConnectHistoryRepository connectHistoryRepository;

	public ConnectHistoryEntity execute(Long newConnectDeviceId, Long routerId, LocalDateTime now) {
		return connectHistoryRepository.save(
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
