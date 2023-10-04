package com.wifi.obs.app.domain.service.wifi;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.repository.wifi.auth.WifiAuthRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteWifiAuthService {

	private final WifiAuthRepository wifiAuthRepository;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(List<Long> auths) {
		wifiAuthRepository.deleteAllById(auths);
	}
}
