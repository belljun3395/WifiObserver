package com.observer.domain.external.dao.history.connect;

import com.observer.data.entity.history.ConnectHistoryEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface ConnectHistoryDao {

	List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
			Long routerId, LocalDateTime createAt);

	ConnectHistoryEntity saveConnectHistory(ConnectHistoryEntity connectHistoryEntity);

	void deleteConnectHistory(ConnectHistoryEntity connectHistoryEntity);
}
