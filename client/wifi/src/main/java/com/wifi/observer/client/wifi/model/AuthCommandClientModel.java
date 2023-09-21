package com.wifi.observer.client.wifi.model;

import com.wifi.observer.client.wifi.model.info.AuthCommandInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class AuthCommandClientModel {

	private final AuthCommandInfo authInfo;
	private final String host;

	public static AuthCommandClientModel fail(String host) {
		return AuthCommandClientModel.builder().authInfo(AuthCommandInfo.fail()).host(host).build();
	}

	public boolean isFail() {
		return authInfo.isFail();
	}
}
