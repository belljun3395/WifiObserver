package com.wifi.obs.client.wifi.model;

import com.wifi.obs.client.wifi.model.value.AuthCommandVO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Auth {

	private final AuthCommandVO authInfo;
	private final String host;

	public static Auth fail(String host) {
		return Auth.builder().authInfo(AuthCommandVO.fail()).host(host).build();
	}

	public boolean isFail() {
		return authInfo.isFail();
	}
}
