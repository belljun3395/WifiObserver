package com.wifi.obs.infra.slack.config;

import lombok.Getter;

@Getter
public enum SlackChannel {
	ERROR("#장애", "장애", "장애가 발생하였습니다."),
	BATCH("#배치", "배치", "배치가 정상적으로 동작 중입니다.");

	private final String channelName;
	private final String type;
	private final String reason;

	SlackChannel(String channelName, String type, String reason) {
		this.channelName = channelName;
		this.type = type;
		this.reason = reason;
	}
}
