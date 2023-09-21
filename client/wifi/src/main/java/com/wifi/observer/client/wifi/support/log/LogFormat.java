package com.wifi.observer.client.wifi.support.log;

import lombok.Getter;

@Getter
public enum LogFormat {
	RESPONSE_ERROR("[{}] response is empty, host : {}", "[%s] response is empty, host : %s");

	private final String logDebugFormat;
	private final String slackFormat;

	LogFormat(String logDebugFormat, String slackFormat) {
		this.logDebugFormat = logDebugFormat;
		this.slackFormat = slackFormat;
	}
}
