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
public class BrowseStepSlackLoggingListener implements StepExecutionListener {

	private static List<String> borwseStepPendingMessageList = new ArrayList<>();

	private final SlackService slackService;

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			slackService.sendSlackMessage(
					"step failed : " + stepExecution.getStepName(), SlackChannel.ERROR);
		}

		addStepCompleteMessage(stepExecution);
		if (LocalDateTime.now().getMinute() == 0) {
			sendPendingMessages();
		}

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

	private void addStepCompleteMessage(StepExecution stepExecution) {
		String stepCompleteMessage = getStepCompleteMessage(stepExecution);
		borwseStepPendingMessageList.add(stepCompleteMessage);
	}

	private void sendPendingMessages() {
		String content = String.join("\n", borwseStepPendingMessageList);
		slackService.sendSlackMessage(content, SlackChannel.BATCH);
		borwseStepPendingMessageList.clear();
	}
}
