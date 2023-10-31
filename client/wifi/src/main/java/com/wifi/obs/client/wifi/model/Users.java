package com.wifi.obs.client.wifi.model;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Users {

	private final List<String> users;
	private final String host;

	public static Users fail(String host) {
		return Users.builder().users(Collections.emptyList()).host(host).build();
	}

	public boolean isFail() {
		return users.isEmpty();
	}
}
