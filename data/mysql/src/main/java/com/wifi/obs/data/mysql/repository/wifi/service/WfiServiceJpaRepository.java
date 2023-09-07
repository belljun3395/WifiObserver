package com.wifi.obs.data.mysql.repository.wifi.service;

import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface WfiServiceJpaRepository extends JpaRepository<WifiServiceEntity, Long> {

	Optional<WifiServiceEntity> findByIdAndDeletedFalse(Long id);

	Optional<WifiServiceEntity> findByWifiAuthEntityAndDeletedFalse(WifiAuthEntity wifiAuthEntity);

	@Query(
			"select w from wifi_service_entity w join fetch w.wifiAuthEntity where w.member = :member and w.deleted = false")
	List<WifiServiceEntity> findAllByMemberAndDeletedFalse(@Param("member") MemberEntity member);
}
