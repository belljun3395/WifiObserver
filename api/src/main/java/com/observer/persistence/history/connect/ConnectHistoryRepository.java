package com.observer.persistence.history.connect;

import com.observer.entity.history.ConnectHistoryEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectHistoryRepository extends JpaRepository<ConnectHistoryEntity, Long> {

	List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
			Long routerId, LocalDateTime createAt);
}
