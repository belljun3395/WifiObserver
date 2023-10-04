package com.wifi.obs.app.web.dto.request.device;

import com.wifi.obs.app.domain.model.device.DeviceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SaveDeviceRequest {

	@NotNull private Long sid;
	@NotNull private DeviceType deviceType;

	@Pattern(regexp = "([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}")
	private String mac;
}
