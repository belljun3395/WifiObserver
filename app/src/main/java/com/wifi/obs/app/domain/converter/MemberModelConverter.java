package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberModelConverter {

	public MemberModel from(MemberEntity source) {
		return MemberModel.builder()
				.id(source.getId())
				.certification(source.getCertification())
				.password(source.getPassword())
				.status(source.getStatus())
				.deviceMaxCount(source.getStatus().getMaxDeviceCount())
				.serviceMaxCount(source.getStatus().getMaxServiceCount())
				.build();
	}

	public MemberEntity toEntity(MemberModel source) {
		return MemberEntity.builder()
				.id(source.getId())
				.certification(source.getCertification())
				.password(source.getPassword())
				.status(source.getStatus())
				.build();
	}
}
