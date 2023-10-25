package com.wifi.obs.infra.batch.support.listener;

import com.wifi.obs.infra.slack.service.BatchNotificationService;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshStepSlackLoggingListener implements StepExecutionListener {

	private final BatchNotificationService batchNotificationService;
	private final ErrorNotificationService errorNotificationService;

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			errorNotificationService.sendNotification("step failed : " + stepExecution.getStepName());
		}

		String stepCompleteMessage = getStepCompleteMessage(stepExecution);
		batchNotificationService.sendNotification(stepCompleteMessage);

		return stepExecution.getExitStatus();
	}

	private String getStepCompleteMessage(StepExecution stepExecution) {
		LocalDateTime now = LocalDateTime.now();
		String stepCompleteMessage =
				String.format(
						"[%d:%d] %s is completed.",
						now.getHour(), now.getMinute(), stepExecution.getStepName());
		return stepCompleteMessage;
	}
}
