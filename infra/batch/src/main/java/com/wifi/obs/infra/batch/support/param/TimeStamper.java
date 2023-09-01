package com.wifi.obs.infra.batch.support.param;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

@Component
public class TimeStamper implements JobParametersIncrementer {
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

	@Override
	public JobParameters getNext(JobParameters parameters) {
		String timeStamp = dateFormat.format(new Date());

		return new JobParametersBuilder(parameters).addString("timeStamp", timeStamp).toJobParameters();
	}
}
