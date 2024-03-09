package com.observer.batch.support.listener;

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
public class BrowseStepLoggingListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (!stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			log.error("step failed : " + stepExecution.getStepName());
		}
		String stepCompleteMessage = getStepCompleteMessage(stepExecution);
		log.info(stepCompleteMessage);
		return stepExecution.getExitStatus();
	}

	private String getStepCompleteMessage(StepExecution stepExecution) {
		LocalDateTime now = LocalDateTime.now();
		return String.format(
				"[%d:%d] %s is completed.", now.getHour(), now.getMinute(), stepExecution.getStepName());
	}
}
