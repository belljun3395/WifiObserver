package com.observer.batch.job.browse.iptime.dao;

import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import java.util.Optional;

public interface ConnectHistoryDao {

	ConnectHistoryEntity save(ConnectHistoryEntity entity);

	Optional<ConnectHistoryEntity> findByDeviceIdAndConnectStatusAndDeletedFalse(
			Long deviceId, ConnectStatus connectStatus);
}
