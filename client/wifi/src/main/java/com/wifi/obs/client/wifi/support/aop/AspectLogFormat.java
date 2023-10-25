package com.wifi.obs.client.wifi.support.aop;

import lombok.Getter;

@Getter
public enum AspectLogFormat {
	START_REQUEST_LOG_FORMAT("Start Request! TraceId is {}"),
	START_BULK_REQUEST_LOG_FORMAT("Start Bulk Request! TraceId is {}"),
	END_REQUEST_LOG_FORMAT("End Request! client log : {}"),
	REQUESTS_LOG_FORMAT("requests count : {} host : {}"),
	REQUEST_LOG_FORMAT("{} : {} host : {}"),
	;

	private final String format;

	AspectLogFormat(String format) {
		this.format = format;
	}
}
