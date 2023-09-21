package com.wifi.observer.client.wifi.model;

import com.wifi.observer.client.wifi.model.info.BrowseQueryInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class BrowseQueryClientModel {

	private final BrowseQueryInfo usersInfo;
	private final String host;

	public static BrowseQueryClientModel fail(String host) {
		return BrowseQueryClientModel.builder().usersInfo(BrowseQueryInfo.fail()).host(host).build();
	}

	public boolean isFail() {
		return usersInfo.isFail();
	}
}
