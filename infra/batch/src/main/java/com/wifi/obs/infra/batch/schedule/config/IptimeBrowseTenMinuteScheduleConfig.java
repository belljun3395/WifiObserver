package com.wifi.obs.infra.batch.schedule.config;

import com.wifi.obs.infra.batch.BatchConfig;
import com.wifi.obs.infra.batch.schedule.jobs.IptimeBrowseScheduledJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.spi.MutableTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IptimeBrowseTenMinuteScheduleConfig {

	private final String REPEAT_10_MINUTE = "0 0/10 * * * ?";

	@Bean(name = BatchConfig.BEAN_NAME_PREFIX + "IptimeBrowseJobDetail")
	public JobDetail quartzJobDetail() {
		return JobBuilder.newJob(IptimeBrowseScheduledJob.class).storeDurably().build();
	}

	@Bean(name = BatchConfig.BEAN_NAME_PREFIX + "IptimeBrowseJobTrigger")
	public Trigger jobTrigger() {
		MutableTrigger cron = CronScheduleBuilder.cronSchedule(REPEAT_10_MINUTE).build();

		return TriggerBuilder.newTrigger()
				.forJob(quartzJobDetail())
				.withSchedule(cron.getScheduleBuilder())
				.build();
	}
}
