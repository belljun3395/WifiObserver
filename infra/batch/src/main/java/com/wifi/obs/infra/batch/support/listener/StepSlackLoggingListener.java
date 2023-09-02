package com.wifi.obs.infra.batch.support.listener;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StepSlackLoggingListener implements StepExecutionListener {

	private static List<String> pendingMessageList = new ArrayList<>();

	private final SlackService slackService;

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			slackService.sendSlackMessage(
					"step failed : " + stepExecution.getStepName(), SlackChannel.ERROR);
		}

		if (pendingMessageList.size() == 6) {
			addStepCompleteMessage(stepExecution);
			sendPendingMessages();
		} else {
			addStepCompleteMessage(stepExecution);
		}

		return stepExecution.getExitStatus();
	}

	private void addStepCompleteMessage(StepExecution stepExecution) {
		LocalDateTime now = LocalDateTime.now();
		String stepCompleteMessage =
				String.format(
						"[%d:%d] %s is completed.",
						now.getHour(), now.getMinute(), stepExecution.getStepName());
		pendingMessageList.add(stepCompleteMessage);
	}

	private void sendPendingMessages() {
		String content = String.join("\n", pendingMessageList);
		slackService.sendSlackMessage(content, SlackChannel.BATCH);
		pendingMessageList.clear();
	}
}
