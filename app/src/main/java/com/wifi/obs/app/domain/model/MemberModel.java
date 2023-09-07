package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.member.MemberEntity;
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

	private MemberEntity source;
	private Long id;
	private String certification;
	private String password;
	private Long deviceMaxCount;
	private Long serviceMaxCount;

	public static MemberModel of(MemberEntity source) {
		return MemberModel.builder()
				.source(source)
				.id(source.getId())
				.certification(source.getCertification())
				.password(source.getPassword())
				.deviceMaxCount(source.getStatus().getMaxDeviceCount())
				.serviceMaxCount(source.getStatus().getMaxServiceCount())
				.build();
	}

	public Boolean isOverDeviceMaxCount(Long count) {
		return count >= deviceMaxCount;
	}

	public Boolean isOverServiceMaxCount(Long count) {
		return count >= serviceMaxCount;
	}

	public Boolean isSamePassword(String request) {
		return password.equals(request);
	}
}
