package com.wifi.obs.data.mysql.repository.history.connect;

import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ConnectHistoryEntity.class, idClass = Long.class)
public interface ConnectHistoryRepository
		extends ConnectHistoryJpaRepository, ConnectHistoryCustomRepository {}
