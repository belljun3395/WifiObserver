package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.usecase.support.manager.GetHealthServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetHealthStatusUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final IdMatchValidator idMatchValidator;

	private final GetHealthServiceManager getHealthServiceManager;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public HttpStatus execute(Long memberId, Long sid) {
		WifiServiceModel service = WifiServiceModel.of(getService(sid));

		idMatchValidator.validate(memberId, service.getMemberId());

		WifiServiceType serviceType = service.getServiceType();
		String host = service.getWifiAuthEntity().getHost();

		return getHealthServiceManager.getService(serviceType).execute(host);
	}

	private WifiServiceEntity getService(Long sid) {
		return wifiServiceRepository.findById(sid).orElseThrow(() -> new ServiceNotFoundException(sid));
	}
}
