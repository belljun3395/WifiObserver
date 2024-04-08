package com.observer.batch.job.browse.iptime.dao;

import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Slf4j
@Profile("!batch-test")
@Repository
@RequiredArgsConstructor
public class ConnectHistoryDaoImpl implements ConnectHistoryDao {

	private final ConnectHistoryRepository connectHistoryRepository;

	@Override
	public ConnectHistoryEntity save(ConnectHistoryEntity entity) {
		return connectHistoryRepository.save(entity);
	}

	@Override
	public Optional<ConnectHistoryEntity> findByDeviceIdAndConnectStatusAndDeletedFalse(
			Long deviceId, ConnectStatus connectStatus) {
		return connectHistoryRepository.findByDeviceIdAndConnectStatusAndDeletedFalse(
				deviceId, connectStatus);
	}
}
