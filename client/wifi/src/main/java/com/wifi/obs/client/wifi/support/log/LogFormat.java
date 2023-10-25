package com.wifi.obs.client.wifi.support.log;

import lombok.Getter;

@Getter
public enum LogFormat {
	RESPONSE_ERROR("[{}] response is empty, host : {}", "[%s] response is empty, host : %s");

	private final String format;
	private final String slackFormat;

	LogFormat(String format, String slackFormat) {
		this.format = format;
		this.slackFormat = slackFormat;
	}
}
