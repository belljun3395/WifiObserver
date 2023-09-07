package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.data.mysql.entity.support.MemberEntitySupporter;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WifiServiceModelConverter {

	private final MemberEntitySupporter memberEntitySupporter;
	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;

	public WifiServiceModel from(WifiServiceEntity source) {
		return WifiServiceModel.builder()
				.id(source.getId())
				.memberId(source.getMember().getId())
				.authId(source.getWifiAuthEntity().getId())
				.serviceType(source.getServiceType())
				.cycle(source.getCycle())
				.standardTime(source.getStandardTime())
				.status(source.getStatus())
				.createdAt(source.getCreateAt())
				.build();
	}

	public WifiServiceEntity toEntity(WifiServiceModel source) {
		return WifiServiceEntity.builder()
				.id(source.getId())
				.member(memberEntitySupporter.getMemberIdEntity(source.getMemberId()))
				.wifiAuthEntity(wifiAuthEntitySupporter.getWifiAuthIdEntity(source.getAuthId()))
				.serviceType(source.getServiceType())
				.cycle(source.getCycle())
				.standardTime(source.getStandardTime())
				.status(source.getStatus())
				.build();
	}
}
