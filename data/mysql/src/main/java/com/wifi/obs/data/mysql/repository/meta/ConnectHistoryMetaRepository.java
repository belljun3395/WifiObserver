package com.wifi.obs.data.mysql.repository.meta;

import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = ConnectHistoryMetaEntity.class, idClass = Long.class)
public interface ConnectHistoryMetaRepository
		extends ConnectHistoryMetaJpaRepository, ConnectHistoryMetaCustomRepository {}
