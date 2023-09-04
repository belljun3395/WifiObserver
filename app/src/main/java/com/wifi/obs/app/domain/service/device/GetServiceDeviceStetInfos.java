package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface GetServiceDeviceStetInfos {

	ServiceDeviceStetInfos execute(
			List<DeviceEntity> devices,
			List<DeviceStetInfo> deviceStetInfos,
			Long serviceId,
			LocalDateTime now);
}
