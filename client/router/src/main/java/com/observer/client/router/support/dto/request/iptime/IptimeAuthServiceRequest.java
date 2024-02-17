package com.observer.client.router.support.dto.request.iptime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class IptimeAuthServiceRequest {

	private final String host;
	private final String userName;
	private final String password;
}
