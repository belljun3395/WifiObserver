package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfos;
import com.wifi.obs.app.domain.usecase.support.manager.GetServiceDeviceStetInfosManager;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
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

	private final WifiServiceConverter wifiServiceConverter;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public ServiceDeviceStetInfos execute(Long memberId, Long sid, StetType type) {

		WifiService service = getWifiService(sid);

		validate(service, memberId);

		return getService(type)
				.execute(
						browseDeviceService.execute(
								wifiServiceEntitySupporter.getReferenceEntity(service.getAuthId())),
						new ArrayList<>(),
						service.getId(),
						LocalDateTime.now());
	}

	private void validate(WifiService service, Long memberId) {
		if (!service.getStatus().isOn()) {
			throw new ClientProblemException();
		}

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

	private GetServiceDeviceStetInfos getService(StetType type) {
		return getServiceDeviceStetInfosManager.getService(type);
	}
}
