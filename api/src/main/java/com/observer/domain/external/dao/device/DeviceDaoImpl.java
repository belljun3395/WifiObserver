package com.observer.domain.external.dao.device;

import com.observer.data.entity.device.DeviceEntity;
import com.observer.data.persistence.device.DeviceRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceDaoImpl implements DeviceDao {

	private final DeviceRepository deviceRepository;

	@Override
	public Optional<DeviceEntity> findByIdAndDeletedFalse(Long id) {
		return deviceRepository.findByIdAndDeletedFalse(id);
	}

	@Override
	public List<DeviceEntity> findAllByRouterIdAndDeletedFalse(Long routerServiceId) {
		return deviceRepository.findAllByRouterIdAndDeletedFalse(routerServiceId);
	}

	@Override
	public DeviceEntity saveDevice(DeviceEntity deviceEntity) {
		return deviceRepository.save(deviceEntity);
	}

	@Override
	public void deleteDevice(DeviceEntity deviceEntity) {
		deviceRepository.delete(deviceEntity);
	}
}
