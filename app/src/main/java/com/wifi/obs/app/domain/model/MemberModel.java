package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberModel {

	private Long id;
	private String certification;
	private String password;
	private MemberStatus status;
	private Long deviceMaxCount;
	private Long serviceMaxCount;

	public boolean isOverDeviceMaxCount(Long count) {
		return count >= deviceMaxCount;
	}

	public boolean isOverServiceMaxCount(Long count) {
		return count >= serviceMaxCount;
	}

	public boolean isSamePassword(String request) {
		return password.equals(request);
	}
}
