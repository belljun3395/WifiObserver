package com.wifi.obs.data.mysql.repository.wifi.auth;

import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WifiAuthJpaRepository extends JpaRepository<WifiAuthEntity, Long> {

	Optional<WifiAuthEntity> findByHostAndDeletedFalse(String host);
}
