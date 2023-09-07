package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.usecase.support.manager.GetServiceDeviceStetInfosManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetServiceStetFacadeUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final BrowseDeviceService browseDeviceService;

	private final GetServiceDeviceStetInfosManager getServiceDeviceStetInfosManager;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public ServiceDeviceStetInfos execute(Long memberId, Long sid, StetType type) {
		WifiServiceEntity service = getService(sid);

		isError(service.getStatus());

		idMatchValidator.validate(memberId, service.getMember().getId());

		return getServiceDeviceStetInfosManager
				.getService(type)
				.execute(browseDeviceService.execute(service), new ArrayList<>(), sid, LocalDateTime.now());
	}

	private WifiServiceEntity getService(Long sid) {
		return wifiServiceRepository.findById(sid).orElseThrow(() -> new ServiceNotFoundException(sid));
	}

	private void isError(WifiStatus status) {
		if (status.equals(WifiStatus.ERROR)) {
			throw new ClientProblemException();
		}
	}
}
