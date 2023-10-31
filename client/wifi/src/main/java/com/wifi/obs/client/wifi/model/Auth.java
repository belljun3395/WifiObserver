package com.wifi.obs.client.wifi.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Auth {

	private final String authInfo;
	private final String host;

	public static Auth fail(String host) {
		return Auth.builder().authInfo(Strings.EMPTY).host(host).build();
	}

	public boolean isFail() {
		return Strings.isEmpty(authInfo);
	}
}
