package com.wifi.obs.data.mysql.repository.history.disConnect;

import com.wifi.obs.data.mysql.entity.history.DisConnectHistoryEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = DisConnectHistoryEntity.class, idClass = Long.class)
public interface DisConnectHistoryRepository
		extends DisConnectHistoryJpaRepository, DisConnectHistoryCustomRepository {}
