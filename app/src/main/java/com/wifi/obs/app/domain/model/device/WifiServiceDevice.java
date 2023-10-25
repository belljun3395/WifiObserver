package com.wifi.obs.app.domain.model.device;

import com.wifi.obs.app.domain.model.ModelId;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
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
public class WifiServiceDevice extends Device {

	private WifiService service;

	public Long getMemberId() {
		return service.getMemberId();
	}

	public WifiServiceType getServiceType() {
		return service.getType();
	}

	public boolean isSameService(Long target) {
		return service.isSameWifiService(target);
	}

	public void changeService(Long newServiceId) {
		super.changeServiceId(newServiceId);
		this.service = service.toBuilder().id(ModelId.of(newServiceId)).build();
	}
}
