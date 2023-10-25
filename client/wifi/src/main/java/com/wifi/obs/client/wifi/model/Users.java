package com.wifi.obs.client.wifi.model;

import com.wifi.obs.client.wifi.model.value.BrowseQueryVO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Users {

	private final BrowseQueryVO usersInfo;
	private final String host;

	public static Users fail(String host) {
		return Users.builder().usersInfo(BrowseQueryVO.fail()).host(host).build();
	}

	public boolean isFail() {
		return usersInfo.isFail();
	}
}
