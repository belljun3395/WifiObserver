package com.wifi.obs.app.domain.model.device;

import com.wifi.obs.app.domain.model.ModelId;
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
public class Device {

	private ModelId id;
	private ModelId serviceId;
	private DeviceType type;
	private String mac;

	public Long getId() {
		return id.getId();
	}

	public Long getServiceId() {
		return serviceId.getId();
	}

	protected void changeServiceId(Long sid) {
		this.serviceId = ModelId.of(sid);
	}
}
