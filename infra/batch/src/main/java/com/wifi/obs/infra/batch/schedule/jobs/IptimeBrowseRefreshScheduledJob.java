package com.wifi.obs.infra.batch.schedule.jobs;

import com.wifi.obs.infra.batch.job.refresh.iptime.IptimeBrowseRefreshConfig;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Configuration
public class IptimeBrowseRefreshScheduledJob extends QuartzJobBean {

	private final Job job;
	private final JobExplorer jobExplorer;
	private final JobLauncher jobLauncher;

	private final ErrorNotificationService errorNotificationService;

	public IptimeBrowseRefreshScheduledJob(
			@Qualifier(value = IptimeBrowseRefreshConfig.JOB_NAME) Job job,
			JobExplorer jobExplorer,
			JobLauncher jobLauncher,
			ErrorNotificationService errorNotificationService) {
		this.job = job;
		this.jobExplorer = jobExplorer;
		this.jobLauncher = jobLauncher;
		this.errorNotificationService = errorNotificationService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobParameters jobParameters =
				new JobParametersBuilder(this.jobExplorer).getNextJobParameters(job).toJobParameters();
		try {
			this.jobLauncher.run(this.job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
			errorNotificationService.sendNotification(
					IptimeBrowseRefreshConfig.JOB_NAME + "\n" + e.getMessage());
		}
	}
}
