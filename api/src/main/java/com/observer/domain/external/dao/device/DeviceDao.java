package com.observer.domain.external.dao.device;

import com.observer.data.entity.device.DeviceEntity;
import java.util.List;
import java.util.Optional;

public interface DeviceDao {
	Optional<DeviceEntity> findByIdAndDeletedFalse(Long id);

	List<DeviceEntity> findAllByRouterIdAndDeletedFalse(Long routerServiceId);

	DeviceEntity saveDevice(DeviceEntity deviceEntity);

	void deleteDevice(DeviceEntity deviceEntity);
}
