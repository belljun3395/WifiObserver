package com.observer.batch.job.browse.iptime.service;

import com.observer.batch.job.utils.ZeroSecondDateTimeCalculator;
import com.observer.data.support.RecordMapper;
import com.observer.data.support.RecordSupportInfo;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRecordService {

	private final RecordMapper recordMapper;

	/** d1 < d2 */
	public String execute(LocalDateTime d1, LocalDateTime d2, RecordSupportInfo recordInfo) {
		if (d1.getMonth() != d2.getMonth()) {
			final LocalDateTime changedMonthStartDateTime =
					LocalDateTime.of(d2.getYear(), d2.getMonth(), 1, 0, 0);
			long beforeNowAccumulateMinutes =
					ZeroSecondDateTimeCalculator.between(d1, changedMonthStartDateTime).toMinutes();
			recordInfo.accumulate(beforeNowAccumulateMinutes);
			return recordMapper.execute(recordInfo);
		}

		long connectedMinutes = ZeroSecondDateTimeCalculator.between(d1, d2).toMinutes();
		if (d2.getDayOfWeek() != d1.getDayOfWeek()) {
			final LocalDateTime changedDayStartDateTime =
					LocalDateTime.of(d1.getYear(), d1.getMonth(), d1.getDayOfMonth(), 0, 0);
			long beforeChangeDayAccumulateMinutes =
					ZeroSecondDateTimeCalculator.between(d2, changedDayStartDateTime).toMinutes();
			recordInfo.accumulate(beforeChangeDayAccumulateMinutes);
			recordInfo.resetDay();
			long remainAccumulateMinutes = connectedMinutes - beforeChangeDayAccumulateMinutes;
			recordInfo.accumulate(remainAccumulateMinutes);
		} else {
			recordInfo.accumulate(connectedMinutes);
		}
		return recordMapper.execute(recordInfo);
	}
}
