package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WifiServiceInfos {

	private final List<WifiServiceInfo> wifiServiceInfos;

	public static WifiServiceInfos of(List<WifiServiceInfo> wifiServiceInfos) {
		return new WifiServiceInfos(wifiServiceInfos);
	}
}
