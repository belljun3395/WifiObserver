package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.DeviceModelConverter;
import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.model.DeviceModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
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

	private final WifiServiceModelConverter wifiServiceModelConverter;
	private final DeviceModelConverter deviceModelConverter;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public WifiServiceInfos execute(Long memberId) {
		List<WifiServiceModel> services = getServices(memberId);

		List<List<DeviceModel>> devices = getDevices(services);

		return getServiceInfos(services, devices);
	}

	private List<List<DeviceModel>> getDevices(List<WifiServiceModel> services) {
		return services.stream()
				.map(
						info ->
								browseDeviceService.execute(
										wifiServiceEntitySupporter.getReferenceEntity(info.getAuthId())))
				.map(
						deviceEntities ->
								deviceEntities.stream()
										.map(deviceModelConverter::from)
										.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	private List<WifiServiceModel> getServices(Long memberId) {
		return wifiServiceRepository
				.findAllByMemberAndDeletedFalse(MemberEntity.builder().id(memberId).build())
				.stream()
				.map(wifiServiceModelConverter::from)
				.collect(Collectors.toList());
	}

	private WifiServiceInfos getServiceInfos(
			List<WifiServiceModel> services, List<List<DeviceModel>> devices) {

		List<WifiServiceInfo> serviceInfos = new ArrayList<>();

		for (int i = 0; i < services.size(); i++) {
			WifiServiceModel cws = services.get(i);
			List<DeviceModel> cds = devices.get(i);

			List<DeviceInfo> deviceInfos = new ArrayList<>();

			for (DeviceModel d : cds) {
				deviceInfos.add(
						DeviceInfo.builder().id(d.getId()).type(d.getDeviceType()).mac(d.getMac()).build());
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
