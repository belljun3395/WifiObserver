package com.wifi.obs.infra.batch.schedule.config;

import com.wifi.obs.infra.batch.BatchConfig;
import com.wifi.obs.infra.batch.schedule.jobs.IptimeBrowseRefreshScheduledJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.spi.MutableTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IptimeBrowseRefreshScheduleConfig {

	private final String BROWSE_REFRESH_TIME = "0 55 23 * * ?";

	@Bean(name = BatchConfig.BEAN_NAME_PREFIX + "IptimeBrowseRefreshScheduledJobDetail")
	public JobDetail quartzJobDetail() {
		return JobBuilder.newJob(IptimeBrowseRefreshScheduledJob.class).storeDurably().build();
	}

	@Bean(name = BatchConfig.BEAN_NAME_PREFIX + "IptimeBrowseRefreshScheduledJobTrigger")
	public Trigger jobTrigger() {
		MutableTrigger cron = CronScheduleBuilder.cronSchedule(BROWSE_REFRESH_TIME).build();

		return TriggerBuilder.newTrigger()
				.forJob(quartzJobDetail())
				.withSchedule(cron.getScheduleBuilder())
				.build();
	}
}
