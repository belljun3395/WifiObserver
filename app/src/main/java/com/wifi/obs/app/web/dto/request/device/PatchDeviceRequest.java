package com.wifi.obs.app.web.dto.request.device;

import javax.validation.constraints.NotNull;
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
public class PatchDeviceRequest {

	@NotNull private Long deviceId;
	@NotNull private Long changeServiceId;
}
