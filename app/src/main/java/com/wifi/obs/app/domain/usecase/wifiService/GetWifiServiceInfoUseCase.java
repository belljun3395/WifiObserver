package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetWifiServiceInfoUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final BrowseDeviceService browseDeviceService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public WifiServiceInfos execute(Long memberId) {
		List<WifiServiceModel> services = getServices(memberId);

		List<List<DeviceEntity>> devices = getDevices(services);

		return getServiceInfos(services, devices);
	}

	private List<List<DeviceEntity>> getDevices(List<WifiServiceModel> services) {
		return services.stream()
				.map(info -> browseDeviceService.execute(info.getSource()))
				.collect(Collectors.toList());
	}

	private List<WifiServiceModel> getServices(Long memberId) {
		return wifiServiceRepository
				.findAllByMemberAndDeletedFalse(MemberEntity.builder().id(memberId).build())
				.stream()
				.map(WifiServiceModel::of)
				.collect(Collectors.toList());
	}

	private WifiServiceInfos getServiceInfos(
			List<WifiServiceModel> services, List<List<DeviceEntity>> devices) {

		List<WifiServiceInfo> serviceInfos = new ArrayList<>();

		for (int i = 0; i < services.size(); i++) {
			WifiServiceModel cws = services.get(i);
			List<DeviceEntity> cds = devices.get(i);

			List<DeviceInfo> deviceInfos = new ArrayList<>();

			for (DeviceEntity d : cds) {
				deviceInfos.add(
						DeviceInfo.builder().id(d.getId()).type(d.getType()).mac(d.getMac()).build());
			}

			serviceInfos.add(
					WifiServiceInfo.builder()
							.id(cws.getId())
							.type(cws.getServiceType())
							.cycle(cws.getCycle())
							.status(cws.getStatus())
							.createAt(cws.getCreateAtAsString())
							.deviceInfos(deviceInfos)
							.build());
		}

		return WifiServiceInfos.of(serviceInfos);
	}
}
