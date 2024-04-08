package com.observer.batch.job.browse.iptime.service;

import com.observer.batch.config.BatchConfig;
import com.observer.batch.config.dao.TestExistNotSameDayDisConnectHistoryDao;
import com.observer.batch.config.dao.TestExistNotSameMonthDisConnectHistoryDao;
import com.observer.batch.config.dao.TestNotExistDisconnectHistoryDao;
import com.observer.data.entity.history.ConnectHistoryEntity;
import com.observer.data.entity.history.ConnectStatus;
import com.observer.data.support.RecordMapper;
import com.observer.data.support.RecordParser;
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
class SaveNewRecordServiceTest {

	@Autowired RecordParser recordParser;

	@Autowired RecordMapper recordMapper;

	@Autowired TestNotExistDisconnectHistoryDao testNotExistDisconnectHistoryDao;

	@Autowired TestExistNotSameMonthDisConnectHistoryDao testExistNotSameMonthDisConnectHistoryDao;

	@Autowired TestExistNotSameDayDisConnectHistoryDao testExistNotSameDayDisConnectHistoryDao;

	@Test
	@DisplayName("이전 연결 기록 및 해지 기록이 없는 경우: 신규 기록 생성")
	void no_disconnect_record_new_record() {
		// Given
		SaveNewRecordService saveNewRecordService =
				new SaveNewRecordService(testNotExistDisconnectHistoryDao, recordParser, recordMapper);
		long newConnectDeviceId = 1L;
		long routerId = 1L;
		LocalDateTime now = LocalDateTime.now();

		// When
		ConnectHistoryEntity response = saveNewRecordService.execute(newConnectDeviceId, routerId, now);

		// Then
		Assertions.assertEquals(newConnectDeviceId, response.getDeviceId());
		Assertions.assertEquals(routerId, response.getRouterId());
		Assertions.assertEquals(now, response.getConnectTime());
		Assertions.assertEquals(now, response.getCheckTime());
		Assertions.assertEquals(now, response.getDisConnectTime());
		Assertions.assertEquals(0L, recordParser.execute(response.getRecord()).getMonth());
		Assertions.assertEquals(0L, recordParser.execute(response.getRecord()).getDay());
		Assertions.assertEquals(ConnectStatus.CONNECTED, response.getConnectStatus());
	}

	@Test
	@DisplayName("월이 다른 해지 기록이 있는 경우: 재접속 기록 생성")
	void not_same_month_disconnect_record_new_record() {
		// Given
		SaveNewRecordService saveNewRecordService =
				new SaveNewRecordService(
						testExistNotSameMonthDisConnectHistoryDao, recordParser, recordMapper);
		long newConnectDeviceId = 1L;
		long routerId = 1L;
		LocalDateTime disconnectTime = TestExistNotSameMonthDisConnectHistoryDao.DISCONNECT_TIME;
		LocalDateTime now =
				LocalDateTime.of(disconnectTime.getYear(), disconnectTime.getMonth().plus(1), 1, 0, 0);

		// When
		ConnectHistoryEntity response = saveNewRecordService.execute(newConnectDeviceId, routerId, now);

		// Then
		Assertions.assertEquals(newConnectDeviceId, response.getDeviceId());
		Assertions.assertEquals(routerId, response.getRouterId());
		Assertions.assertEquals(now, response.getConnectTime());
		Assertions.assertEquals(now, response.getCheckTime());
		Assertions.assertEquals(now, response.getDisConnectTime());
		Assertions.assertEquals(0L, recordParser.execute(response.getRecord()).getMonth());
		Assertions.assertEquals(0L, recordParser.execute(response.getRecord()).getDay());
		Assertions.assertEquals(ConnectStatus.CONNECTED, response.getConnectStatus());
	}

	@Test
	@DisplayName("일이 다른 해지 기록이 있는 경우: 재접속 기록 생성")
	void not_same_day_disconnect_record_new_record() {
		// Given
		SaveNewRecordService saveNewRecordService =
				new SaveNewRecordService(
						testExistNotSameDayDisConnectHistoryDao, recordParser, recordMapper);
		long newConnectDeviceId = 1L;
		long routerId = 1L;
		LocalDateTime disconnectTime = TestExistNotSameDayDisConnectHistoryDao.DISCONNECT_TIME;
		LocalDateTime now =
				LocalDateTime.of(
						disconnectTime.getYear(),
						disconnectTime.getMonth(),
						disconnectTime.getDayOfMonth() + 1,
						0,
						0);

		// When
		ConnectHistoryEntity response = saveNewRecordService.execute(newConnectDeviceId, routerId, now);

		// Then
		Assertions.assertEquals(newConnectDeviceId, response.getDeviceId());
		Assertions.assertEquals(routerId, response.getRouterId());
		Assertions.assertEquals(now, response.getConnectTime());
		Assertions.assertEquals(now, response.getCheckTime());
		Assertions.assertEquals(now, response.getDisConnectTime());
		Assertions.assertEquals(
				TestExistNotSameDayDisConnectHistoryDao.ACCUMULATE_MINUTES,
				recordParser.execute(response.getRecord()).getMonth());
		Assertions.assertEquals(0L, recordParser.execute(response.getRecord()).getDay());
		Assertions.assertEquals(ConnectStatus.CONNECTED, response.getConnectStatus());
	}
}
