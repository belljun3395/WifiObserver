package com.wifi.obs.data.mysql.repository.history.disConnect;

import com.wifi.obs.data.mysql.entity.history.DisConnectHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DisConnectHistoryJpaRepository
		extends JpaRepository<DisConnectHistoryEntity, Long> {}
