package com.observer.domain.service.router.support;

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
public class RouterInfoSupport {

	private Long memberId;
	private Long routerId;
	private String serviceType;
	@Builder.Default private Long cycle = 10L;
	@Builder.Default private Long standardTime = 9L;
	private String host;
	private String certification;
	private String password;
}
