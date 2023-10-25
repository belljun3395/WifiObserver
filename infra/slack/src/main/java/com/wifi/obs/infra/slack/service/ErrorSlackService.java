package com.wifi.obs.infra.slack.service;

import com.wifi.obs.infra.slack.config.SlackChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorSlackService implements ErrorNotificationService {

	private final SlackService slackService;

	@Override
	public void sendNotification(String message) {
		slackService.sendNotification(message, SlackChannel.ERROR);
	}
}
