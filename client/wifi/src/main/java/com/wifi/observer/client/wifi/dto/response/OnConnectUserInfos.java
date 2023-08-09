package com.wifi.observer.client.wifi.dto.response;

import java.util.List;
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
public class OnConnectUserInfos {

	private final List<OnConnectUserInfo> users;
	private final String host;
}
