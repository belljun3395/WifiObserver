package com.observer.batch.config.dao;

import com.observer.batch.job.browse.iptime.dao.ConnectHistoryDao;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import java.util.Optional;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestNotExistDisconnectHistoryDao implements ConnectHistoryDao {

	@Override
	public ConnectHistoryEntity save(ConnectHistoryEntity entity) {
		return entity;
	}

	@Override
	public Optional<ConnectHistoryEntity> findByDeviceIdAndConnectStatusAndDeletedFalse(
			Long deviceId, ConnectStatus connectStatus) {
		return Optional.empty();
	}
}
