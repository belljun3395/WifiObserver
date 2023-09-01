package com.wifi.obs.infra.slack.config;

import lombok.Getter;

@Getter
public enum SlackChannel {
	ERROR("#장애"),
	BATCH("#배치");

	private final String channelName;

	SlackChannel(String channelName) {
		this.channelName = channelName;
	}
}
