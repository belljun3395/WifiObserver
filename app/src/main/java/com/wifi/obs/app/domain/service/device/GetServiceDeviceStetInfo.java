package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import java.time.LocalDateTime;

public interface GetServiceDeviceStetInfo {

	DeviceStetInfo execute(DeviceEntity device, LocalDateTime now);
}
