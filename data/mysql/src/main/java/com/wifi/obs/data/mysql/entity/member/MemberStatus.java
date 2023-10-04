package com.wifi.obs.data.mysql.entity.member;

import lombok.Getter;

@Getter
public enum MemberStatus {
	NORMAL("NORMAL", 1L, 30L),
	WITHDRAWN("WITHDRAW", 0L, 0L);

	private String type;
	private Long maxServiceCount;
	private Long maxDeviceCount;

	MemberStatus(String type, Long maxServiceCount, Long maxDeviceCount) {
		this.type = type;
		this.maxServiceCount = maxServiceCount;
		this.maxDeviceCount = maxDeviceCount;
	}
}
