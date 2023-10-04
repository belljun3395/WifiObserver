package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.member.MemberStatus;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberConverter {

	public Member from(MemberEntity source) {
		return Member.builder()
				.id(source.getId())
				.certification(source.getCertification())
				.password(source.getPassword())
				.status(MemberStatus.valueOf(source.getStatus().getType()))
				.deviceMax(source.getStatus().getMaxDeviceCount())
				.serviceMax(source.getStatus().getMaxServiceCount())
				.build();
	}
}
