package com.wifi.obs.data.mysql.repository.device;

import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, Long> {

	Optional<DeviceEntity> findByIdAndDeletedFalse(Long id);

	Boolean existsByMacAndWifiServiceAndDeletedFalse(String mac, WifiServiceEntity wifiService);

	List<DeviceEntity> findAllByWifiServiceAndDeletedFalse(WifiServiceEntity wifiService);

	Optional<DeviceEntity> findByMacAndDeletedFalse(String mac);
}
