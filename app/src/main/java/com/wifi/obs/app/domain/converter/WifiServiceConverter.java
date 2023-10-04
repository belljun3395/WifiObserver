package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
import com.wifi.obs.app.domain.model.wifi.WifiStatus;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WifiServiceConverter {

	private final WifiAuthConverter wifiAuthConverter;

	public WifiService from(WifiServiceEntity source) {
		return WifiService.builder()
				.id(source.getId())
				.memberId(source.getMember().getId())
				.auth(wifiAuthConverter.from(source.getWifiAuthEntity()))
				.type(WifiServiceType.valueOf(source.getServiceType().getType()))
				.cycle(source.getCycle())
				.standardTime(source.getStandardTime())
				.status(WifiStatus.valueOf(source.getStatus().getType()))
				.createdAt(source.getCreateAt())
				.build();
	}

	public List<WifiService> from(List<WifiServiceEntity> source) {
		return source.stream().map(this::from).collect(java.util.stream.Collectors.toList());
	}
}
