package com.wifi.obs.infra.batch.support.listener;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
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

	private final SlackService slackService;

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			slackService.sendSlackMessage(
					"step failed : " + stepExecution.getStepName(), SlackChannel.ERROR);
		}

		String stepCompleteMessage = getStepCompleteMessage(stepExecution);
		slackService.sendSlackMessage(stepCompleteMessage, SlackChannel.BATCH);

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
