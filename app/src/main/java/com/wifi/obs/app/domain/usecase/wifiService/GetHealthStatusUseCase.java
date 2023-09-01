package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.service.wifi.iptime.GetIptimeHealthService;
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

	private final GetIptimeHealthService getIptimeHealthService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public HttpStatus execute(Long memberId, Long sid) {
		WifiServiceEntity service =
				wifiServiceRepository
						.findById(sid)
						.orElseThrow(() -> new RuntimeException("존재하지 않는 서비스입니다."));

		if (!memberId.equals(service.getMember().getId())) {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}

		WifiServiceType serviceType = service.getServiceType();
		String host = service.getWifiAuthEntity().getHost();

		if (serviceType.equals(WifiServiceType.IPTIME)) {
			return getIptimeHealthService.execute(host);
		}

		throw new RuntimeException("지원하지 않는 서비스입니다.");
	}
}
