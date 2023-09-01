package com.wifi.obs.data.mysql.repository.wifi.service;

import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = WifiServiceEntity.class, idClass = Long.class)
public interface WifiServiceRepository
		extends WfiServiceJpaRepository, WifiServiceCustomRepository {}
