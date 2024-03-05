package com.observer.web.controller.dto.router;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PostRouterRequest {
	@Builder.Default private String serviceType = "IPTIME";
	private String host;
	private String certification;
	private String password;
}
