package com.wifi.observer.client.wifi.support.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WifiLogData {

	private String traceId;

	private String timestamp;

	private String duration;

	private String actor;

	private String tag;

	@Default private String message = "success client request";
}
