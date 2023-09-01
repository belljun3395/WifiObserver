package com.wifi.obs.data.mysql.repository.history.connect;

import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ConnectHistoryJpaRepository extends JpaRepository<ConnectHistoryEntity, Long> {

	List<ConnectHistoryEntity> findAllByWifiServiceAndCreateAtAfterAndDeletedFalse(
			WifiServiceEntity service, LocalDateTime standardTime);
}
