package com.observer.batch.job.browse.iptime.service;

import com.observer.batch.config.BatchConfig;
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
class GetRecordServiceTest {

	@Autowired GetRecordService getRecordService;

	@Autowired RecordParser recordParser;

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 일과 월이 바뀌지 않은 경우")
	void not_change_case() {
		// Given
		LocalDateTime firstDayOfMonth = LocalDateTime.of(2024, 3, 1, 0, 0);
		long MINUTES_GAP = 10L;
		LocalDateTime d1 = firstDayOfMonth.plusMinutes(MINUTES_GAP);
		LocalDateTime d2 = firstDayOfMonth.plusMinutes(MINUTES_GAP * 2);
		long MONTH_VALUE = 10L;
		long DAY_VALUE = 10L;
		RecordSupportInfo record =
				RecordSupportInfo.builder().month(MONTH_VALUE).day(DAY_VALUE).build();

		// When
		RecordSupportInfo updatedRecord =
				recordParser.execute(getRecordService.execute(d1, d2, record));

		// Then
		Assertions.assertEquals(MONTH_VALUE + MINUTES_GAP, updatedRecord.getMonth());
		Assertions.assertEquals(DAY_VALUE + MINUTES_GAP, updatedRecord.getDay());
	}

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 월이 바뀐 경우: 월 단위로 누적 시간 계산")
	void change_month_case() {
		// Given
		LocalDateTime firstDayOfMonth = LocalDateTime.of(2024, 3, 1, 0, 0);
		long MINUTES_GAP = 10L;
		LocalDateTime d1 = firstDayOfMonth.minusMinutes(MINUTES_GAP);
		LocalDateTime d2 = firstDayOfMonth;
		long MONTH_VALUE = 10L;
		long DAY_VALUE = 10L;
		RecordSupportInfo record =
				RecordSupportInfo.builder().month(MONTH_VALUE).day(DAY_VALUE).build();

		// When
		RecordSupportInfo updatedRecord =
				recordParser.execute(getRecordService.execute(d1, d2, record));

		// Then
		Assertions.assertEquals(MONTH_VALUE + MINUTES_GAP, updatedRecord.getMonth());
		Assertions.assertEquals(DAY_VALUE + MINUTES_GAP, updatedRecord.getDay());
	}

	@Test
	@DisplayName("접속한 시간과 접속이 끊긴 시간 사이에 일이 바뀐 경우")
	void change_day_case() {
		// Given
		LocalDateTime secondDayOfMonth = LocalDateTime.of(2024, 3, 2, 0, 0);
		long MINUTES_GAP = 10L;
		LocalDateTime d1 = secondDayOfMonth.minusMinutes(MINUTES_GAP);
		LocalDateTime d2 = secondDayOfMonth;
		long MONTH_VALUE = 10L;
		long DAY_VALUE = 10L;
		RecordSupportInfo record =
				RecordSupportInfo.builder().month(MONTH_VALUE).day(DAY_VALUE).build();

		// When
		RecordSupportInfo updatedRecord =
				recordParser.execute(getRecordService.execute(d1, d2, record));

		// Then
		Assertions.assertEquals(MONTH_VALUE + MINUTES_GAP, updatedRecord.getMonth());
		Assertions.assertEquals(0L, updatedRecord.getDay());
	}
}
