package com.wifi.obs.data.mysql.repository.meta;

import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ConnectHistoryMetaJpaRepository
		extends JpaRepository<ConnectHistoryMetaEntity, Long> {

	Optional<ConnectHistoryMetaEntity> findTopByDeviceAndDayAndMonthOrderByIdDesc(
			DeviceEntity device, Long day, Long month);

	Optional<ConnectHistoryMetaEntity> findTopByDeviceAndMonthOrderByIdDesc(
			DeviceEntity device, Long month);
}
