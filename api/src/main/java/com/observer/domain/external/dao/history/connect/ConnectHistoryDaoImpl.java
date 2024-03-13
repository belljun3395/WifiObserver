package com.observer.domain.external.dao.history.connect;

import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ConnectHistoryDaoImpl implements ConnectHistoryDao {

	private final ConnectHistoryRepository connectHistoryRepository;

	@Override
	public List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
			Long routerId, LocalDateTime createAt) {
		return connectHistoryRepository.findAllByRouterIdAndCreateAtAfterAndDeletedFalse(
				routerId, createAt);
	}

	@Override
	public List<ConnectHistoryEntity> findAllByRouterIdAndCreateAtBeforeAndDeletedFalse(
			Long routerId, LocalDateTime createAt) {
		return connectHistoryRepository.findAllByRouterIdAndCreateAtBeforeAndDeletedFalse(
				routerId, createAt);
	}

	@Override
	public ConnectHistoryEntity saveConnectHistory(ConnectHistoryEntity connectHistoryEntity) {
		return connectHistoryRepository.save(connectHistoryEntity);
	}

	@Override
	public void deleteConnectHistory(ConnectHistoryEntity connectHistoryEntity) {
		connectHistoryRepository.delete(connectHistoryEntity);
	}
}
