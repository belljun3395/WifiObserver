package com.observer.data.persistence.history.connect;

import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectHistoryRepository extends JpaRepository<ConnectHistoryEntity, Long> {

	List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
			Long routerId, LocalDateTime createAt);

	List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtBeforeAndDeletedFalse(
			Long routerId, LocalDateTime createAt);

	List<ConnectHistoryEntity> findAllByRouterIdAndConnectStatusAndDeletedFalse(
			Long routerId, ConnectStatus connectStatus);
}
