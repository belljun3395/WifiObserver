package com.observer.domain.dto.router;

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
public class PostRouterUseCaseRequest {

	private String apiKey;
	private String serviceType;
	private String host;
	private String certification;
	private String password;
}
