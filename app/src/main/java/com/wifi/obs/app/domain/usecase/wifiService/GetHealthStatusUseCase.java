package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.usecase.support.manager.GetHealthServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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

	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public HttpStatus execute(Long memberId, Long sid) {

		WifiServiceModel service = wifiServiceModelConverter.from(getService(sid));

		idMatchValidator.validate(memberId, service.getMemberId());

		String host = wifiAuthEntitySupporter.getReferenceEntity(service.getAuthId()).getHost();

		return getHealthServiceManager.getService(service.getServiceType()).execute(host);
	}

	private WifiServiceEntity getService(Long sid) {
		return wifiServiceRepository
				.findByIdAndDeletedFalse(sid)
				.orElseThrow(() -> new ServiceNotFoundException(sid));
	}
}
