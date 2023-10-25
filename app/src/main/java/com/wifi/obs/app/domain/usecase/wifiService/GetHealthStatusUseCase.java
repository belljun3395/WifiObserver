package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.obs.app.domain.usecase.support.manager.GetHealthServiceManager;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
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

	private final GetHealthServiceManager getHealthServiceManager;

	private final WifiServiceConverter wifiServiceConverter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public HttpStatus execute(Long memberId, Long sid) {

		WifiService service = getWifiService(sid);

		validate(service, memberId);

		return getService(service).execute(service.getHost());
	}

	private void validate(WifiService service, Long memberId) {
		if (service.isServiceOwner(memberId)) {
			throw new NotMatchInformationException();
		}
	}

	private WifiService getWifiService(Long sid) {
		return wifiServiceConverter.from(
				wifiServiceRepository
						.findByIdAndDeletedFalse(sid)
						.orElseThrow(() -> new ServiceNotFoundException(sid)));
	}

	private GetHealthService getService(WifiService service) {
		return getHealthServiceManager.getService(service.getType());
	}
}
