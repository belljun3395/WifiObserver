package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.Map;
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

	private final Map<String, GetHealthService> getHealthServiceMap;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public HttpStatus execute(Long memberId, Long sid) {
		WifiServiceEntity service =
				wifiServiceRepository.findById(sid).orElseThrow(() -> new ServiceNotFoundException(sid));

		if (!memberId.equals(service.getMember().getId())) {
			throw new NotMatchInformationException();
		}

		WifiServiceType serviceType = service.getServiceType();
		String host = service.getWifiAuthEntity().getHost();

		return getMatchServiceTypeHealthService(serviceType).execute(host);
	}

	public GetHealthService getMatchServiceTypeHealthService(WifiServiceType type) {
		String key =
				getHealthServiceMap.keySet().stream()
						.filter(s -> s.contains(type.getType()))
						.findFirst()
						.orElseThrow(BadTypeRequestException::new);

		return getHealthServiceMap.get(key);
	}
}
