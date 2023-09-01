package com.wifi.obs.data.mysql.repository.wifi.auth;

import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = WifiAuthEntity.class, idClass = Long.class)
public interface WifiAuthRepository extends WifiAuthJpaRepository, WifiAuthCustomRepository {}
