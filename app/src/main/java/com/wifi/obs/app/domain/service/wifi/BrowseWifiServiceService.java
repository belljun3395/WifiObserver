package com.wifi.obs.app.domain.service.wifi;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrowseWifiServiceService {

	private final WifiServiceRepository wifiServiceRepository;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public List<WifiServiceEntity> execute(MemberEntity member) {
		return new ArrayList<>(wifiServiceRepository.findAllByMemberAndDeletedFalse(member));
	}
}
