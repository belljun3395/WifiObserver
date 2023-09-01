package com.wifi.obs.data.mysql.repository.device;

import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = DeviceEntity.class, idClass = Long.class)
public interface DeviceRepository extends DeviceCustomRepository, DeviceJpaRepository {}
