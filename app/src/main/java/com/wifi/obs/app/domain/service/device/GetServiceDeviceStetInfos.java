package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.model.device.Device;
import java.time.LocalDateTime;
import java.util.List;

public interface GetServiceDeviceStetInfos {

	ServiceDeviceStetInfos execute(
			List<Device> devices, List<DeviceStetInfo> stetInfos, Long sid, LocalDateTime now);
}
