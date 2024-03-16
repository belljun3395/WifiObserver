package com.observer.batch.job.browse.iptime.service;

import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import com.observer.data.support.RecordParser;
import com.observer.data.support.RecordSupportInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveDisconnectRecordService {

	private final ConnectHistoryRepository connectHistoryRepository;

	private final GetRecordService getRecordService;

	private final RecordParser recordParser;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(ConnectHistoryEntity historyEntity, LocalDateTime now) {
		final LocalDateTime lastConnectDateTime = historyEntity.getCheckTime();
		final String record = historyEntity.getRecord();

		RecordSupportInfo recordSupportInfo = recordParser.execute(record);

		String newRecord = getRecordService.execute(lastConnectDateTime, now, recordSupportInfo);

		connectHistoryRepository.save(
				historyEntity.toBuilder()
						.disConnectTime(now)
						.connectStatus(ConnectStatus.DISCONNECTED)
						.record(newRecord)
						.build());
	}
}
