package com.wifi.obs.infra.slack.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.wifi.obs.infra.slack.config.SlackChannel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackService {

	@Value(value = "${slack.token}")
	private String token;

	public void sendSlackMessage(String message, SlackChannel channel) {
		MethodsClient methods = Slack.getInstance().methods(token);

		ChatPostMessageRequest request =
				ChatPostMessageRequest.builder().channel(channel.getChannelName()).text(message).build();

		try {
			methods.chatPostMessage(request);
		} catch (SlackApiException | IOException e) {
			log.error(e.getMessage());
		}

		log.info("Slack message sent. Channel: {}, Message: {}", channel.getChannelName(), message);
	}
}
