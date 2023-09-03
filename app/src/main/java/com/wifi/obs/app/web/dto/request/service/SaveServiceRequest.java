package com.wifi.obs.app.web.dto.request.service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SaveServiceRequest {

	@NotNull private ServiceType type;
	@Builder.Default private Long cycle = 10L;

	@Pattern(
			regexp = "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}")
	private String host;

	@Min(1)
	@Max(9999)
	private Long port;

	@NotNull private String certification;
	@NotNull private String password;

	public String getHost() {
		return host + ":" + port;
	}
}
