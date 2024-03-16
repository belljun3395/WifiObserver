package com.observer.batch.job.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ZeroSecondDateTimeCalculator {

	public static Duration between(LocalDateTime start, LocalDateTime end) {
		LocalDateTime zeroSecondStart =
				LocalDateTime.of(
						start.getYear(),
						start.getMonth(),
						start.getDayOfMonth(),
						start.getHour(),
						start.getMinute());
		return Duration.between(zeroSecondStart, end);
	}
}
