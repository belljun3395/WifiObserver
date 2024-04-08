package com.observer.batch.job.browse.iptime.service;

import com.observer.batch.config.BatchConfig;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.support.RecordParser;
import com.observer.data.support.RecordSupportInfo;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles(value = {"test", "batch-test"})
@SpringBootTest
@ContextConfiguration(classes = {BatchConfig.class})
@TestPropertySource("classpath:application-test.yml")
class SaveDisconnectRecordServiceTest {

	@Autowired SaveDisconnectRecordService saveDisconnectRecordService;

	@Autowired RecordParser recordParser;

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 일과 월이 바뀌지 않은 경우")
	void not_change_case() {
		// Given
		LocalDateTime firstDayOfMonth = LocalDateTime.of(2024, 3, 1, 0, 0);
		long CONNECT_MINUTES_GAP = 60L;
		long DISCONNECT_MINUTES_GAP = 70L;
		long MINUTES_GAP = DISCONNECT_MINUTES_GAP - CONNECT_MINUTES_GAP;
		LocalDateTime connectTime = firstDayOfMonth.plusMinutes(CONNECT_MINUTES_GAP);
		ConnectHistoryEntity historyEntity =
				ConnectHistoryEntity.builder()
						.id(1L)
						.deviceId(1L)
						.routerId(1L)
						.connectTime(connectTime)
						.checkTime(connectTime)
						.disConnectTime(connectTime)
						.build();
		LocalDateTime disconnect = firstDayOfMonth.plusMinutes(DISCONNECT_MINUTES_GAP);

		// When
		ConnectHistoryEntity response = saveDisconnectRecordService.execute(historyEntity, disconnect);

		// Then
		RecordSupportInfo record = recordParser.execute(response.getRecord());
		Assertions.assertEquals(ConnectStatus.DISCONNECTED, response.getConnectStatus());
		Assertions.assertEquals(connectTime, response.getConnectTime());
		Assertions.assertEquals(disconnect, response.getDisConnectTime());
		Assertions.assertEquals(MINUTES_GAP, record.getMonth());
		Assertions.assertEquals(MINUTES_GAP, record.getDay());
	}

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 월이 바뀐 경우: 월 단위로 누적 시간 계산")
	void change_month_case() {
		// Given
		long BEFORE_MONTH_MINUTES_GAP = 10L;
		LocalDateTime firstDayOfMonth = LocalDateTime.of(2024, 3, 1, 0, 0);
		LocalDateTime beforeMonthConnectTime = firstDayOfMonth.minusMinutes(BEFORE_MONTH_MINUTES_GAP);
		ConnectHistoryEntity historyEntity =
				ConnectHistoryEntity.builder()
						.id(1L)
						.deviceId(1L)
						.routerId(1L)
						.connectTime(beforeMonthConnectTime)
						.checkTime(firstDayOfMonth)
						.disConnectTime(beforeMonthConnectTime)
						.build();
		LocalDateTime afterMonthDisconnectTime = firstDayOfMonth;

		// When
		ConnectHistoryEntity response =
				saveDisconnectRecordService.execute(historyEntity, afterMonthDisconnectTime);

		// Then
		RecordSupportInfo record = recordParser.execute(response.getRecord());
		Assertions.assertEquals(ConnectStatus.DISCONNECTED, response.getConnectStatus());
		Assertions.assertEquals(beforeMonthConnectTime, response.getConnectTime());
		Assertions.assertEquals(afterMonthDisconnectTime, response.getDisConnectTime());
		Assertions.assertEquals(0L, record.getMonth());
		Assertions.assertEquals(0L, record.getDay());
	}

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 일이 바뀐 경우")
	void change_day_case() {
		// Given
		LocalDateTime secondDayOfMonth = LocalDateTime.of(2024, 3, 2, 0, 0);
		long MINUTES_GAP = 10L;
		LocalDateTime connectTime = secondDayOfMonth.minusMinutes(MINUTES_GAP);
		ConnectHistoryEntity historyEntity =
				ConnectHistoryEntity.builder()
						.id(1L)
						.deviceId(1L)
						.routerId(1L)
						.connectTime(connectTime)
						.checkTime(connectTime)
						.disConnectTime(connectTime)
						.build();
		LocalDateTime disconnectTime = secondDayOfMonth;

		// When
		ConnectHistoryEntity response =
				saveDisconnectRecordService.execute(historyEntity, disconnectTime);

		// Then
		RecordSupportInfo record = recordParser.execute(response.getRecord());
		Assertions.assertEquals(ConnectStatus.DISCONNECTED, response.getConnectStatus());
		Assertions.assertEquals(connectTime, response.getConnectTime());
		Assertions.assertEquals(disconnectTime, response.getDisConnectTime());
		Assertions.assertEquals(MINUTES_GAP, record.getMonth());
		Assertions.assertEquals(0L, record.getDay());
	}
}
