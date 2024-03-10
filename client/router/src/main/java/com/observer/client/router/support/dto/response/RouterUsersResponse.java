package com.observer.client.router.support.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class RouterUsersResponse extends BasicWifiServiceResponse {

	private final String host;
	private final RouterUsers response;
}
