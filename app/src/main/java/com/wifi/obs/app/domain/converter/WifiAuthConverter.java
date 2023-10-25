package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.ModelId;
import com.wifi.obs.app.domain.model.wifi.WifiAuth;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WifiAuthConverter {

	public WifiAuth from(WifiAuthEntity source) {
		return WifiAuth.builder()
				.id(ModelId.of(source.getId()))
				.host(source.getHost())
				.certification(source.getCertification())
				.password(source.getPassword())
				.build();
	}
}
