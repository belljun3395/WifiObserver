package com.observer.batch.config.dao;

import com.observer.batch.job.browse.iptime.dao.ConnectHistoryDao;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import java.util.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;

@Primary
@TestComponent
public class TestConnectHistoryDao implements ConnectHistoryDao {

	@Override
	public ConnectHistoryEntity save(ConnectHistoryEntity entity) {
		return entity;
	}

	@Override
	public Optional<ConnectHistoryEntity> findByDeviceIdAndConnectStatusAndDeletedFalse(
			Long deviceId, ConnectStatus connectStatus) {
		throw new NotImplementedException();
	}
}
