package com.observer.batch.job.browse.iptime.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.persistence.history.connect.ConnectHistoryRepository;
import com.observer.data.support.RecordMapper;
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
public class CheckRecordService {
	private final ConnectHistoryRepository connectHistoryRepository;

	private final GetRecordService getRecordService;

	private final RecordParser recordParser;
	private final RecordMapper recordMapper;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(ConnectHistoryEntity historyEntity, LocalDateTime now) {
		final LocalDateTime lastConnectDateTime = historyEntity.getCheckTime();
		final String record = historyEntity.getRecord();

		RecordSupportInfo recordSupportInfo = recordParser.execute(record);

		// 접속한 시간과 접속이 끊긴 시간 사이에 월이 바뀐 경우: 월 단위로 누적 시간 계산
		if (lastConnectDateTime.getMonth() != now.getMonth()) {
			getRecordService.execute(lastConnectDateTime, now, recordSupportInfo);
			recordSupportInfo.resetMonth();
			String beforeNowRecord = recordMapper.execute(recordSupportInfo);

			LocalDateTime changedMonthStartDateTime =
					LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
			// 이전 월까지의 누적 시간을 기록
			connectHistoryRepository.save(
					historyEntity.toBuilder()
							.disConnectTime(changedMonthStartDateTime)
							.connectStatus(ConnectStatus.DISCONNECTED)
							.record(beforeNowRecord)
							.build());

			// 이후 월부터의 누적 시간 계산
			// 새로운 레코드로 저장
			String newRecord =
					getRecordService.execute(changedMonthStartDateTime, now, recordSupportInfo);
			connectHistoryRepository.save(
					ConnectHistoryEntity.builder()
							.deviceId(historyEntity.getDeviceId())
							.routerId(historyEntity.getRouterId())
							.connectTime(now)
							.checkTime(now)
							.disConnectTime(now)
							.connectStatus(ConnectStatus.CONNECTED)
							.record(newRecord)
							.build());
			return;
		}

		String newRecord = getRecordService.execute(lastConnectDateTime, now, recordSupportInfo);
		connectHistoryRepository.save(
				historyEntity.toBuilder()
						.checkTime(now)
						.connectStatus(ConnectStatus.CONNECTED)
						.record(newRecord)
						.build());
	}
}
