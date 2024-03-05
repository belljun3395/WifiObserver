package com.observer.persistence.device;

import com.observer.entity.device.DeviceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
	Optional<DeviceEntity> findByIdAndDeletedFalse(Long id);

	List<DeviceEntity> findAllByRouterIdAndDeletedFalse(Long routerServiceId);

	Boolean existsByMacAndRouterIdAndDeletedFalse(String mac, Long routerServiceId);

	Optional<DeviceEntity> findByMacAndDeletedFalse(String mac);
}
