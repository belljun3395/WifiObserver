package com.observer.client.router.support.dto.request.iptime;

import com.observer.client.router.support.dto.request.BasicWifiServiceRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class IptimeAuthServiceRequest extends BasicWifiServiceRequest {

	private final String userName;
	private final String password;
}
