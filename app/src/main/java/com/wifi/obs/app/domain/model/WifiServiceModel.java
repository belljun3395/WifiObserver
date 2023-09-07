package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
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
public class WifiServiceModel {

	private WifiServiceEntity source;
	private Long id;
	private WifiServiceType serviceType;
	private Long cycle;
	private WifiStatus status;

	public static WifiServiceModel of(WifiServiceEntity source) {
		return WifiServiceModel.builder()
				.source(source)
				.id(source.getId())
				.serviceType(source.getServiceType())
				.cycle(source.getCycle())
				.status(source.getStatus())
				.build();
	}

	public Long getMemberId() {
		return source.getMember().getId();
	}

	public WifiAuthEntity getWifiAuthEntity() {
		return source.getWifiAuthEntity();
	}

	public String getCreateAtAsString() {
		return source.getCreateAt().toString();
	}

	public Boolean isOn() {
		return status == WifiStatus.ON;
	}
}
