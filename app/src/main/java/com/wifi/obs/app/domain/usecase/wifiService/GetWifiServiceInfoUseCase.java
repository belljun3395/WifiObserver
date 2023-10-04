package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.DeviceConverter;
import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.model.device.Device;
import com.wifi.obs.app.domain.model.wifi.WifiService;
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

	private final WifiServiceConverter wifiServiceConverter;
	private final DeviceConverter deviceConverter;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public WifiServiceInfos execute(Long memberId) {

		List<WifiService> services = getWifiServices(memberId);

		List<List<Device>> devices = getDevices(services);

		return getServiceInfos(services, devices);
	}

	private List<List<Device>> getDevices(List<WifiService> services) {
		return services.stream()
				.map(
						info ->
								browseDeviceService.execute(
										wifiServiceEntitySupporter.getReferenceEntity(info.getAuthId())))
				.map(
						deviceEntities ->
								deviceEntities.stream().map(deviceConverter::from).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	private List<WifiService> getWifiServices(Long memberId) {
		return wifiServiceRepository
				.findAllByMemberAndDeletedFalse(MemberEntity.builder().id(memberId).build())
				.stream()
				.map(wifiServiceConverter::from)
				.collect(Collectors.toList());
	}

	protected WifiServiceInfos getServiceInfos(
			List<WifiService> wifiServiceSources, List<List<Device>> deviceSources) {

		List<WifiServiceInfo> services = new ArrayList<>();

		for (int i = 0; i < wifiServiceSources.size(); i++) {
			WifiService cws = wifiServiceSources.get(i);
			List<Device> cds = deviceSources.get(i);

			List<DeviceInfo> devices = new ArrayList<>();

			for (Device d : cds) {
				devices.add(DeviceInfo.builder().id(d.getId()).type(d.getType()).mac(d.getMac()).build());
			}

			services.add(
					WifiServiceInfo.builder()
							.id(cws.getId())
							.type(cws.getType())
							.cycle(cws.getCycle())
							.status(cws.getStatus())
							.createAt(cws.getCreatedAt().toString())
							.devices(devices)
							.build());
		}

		return WifiServiceInfos.of(services);
	}
}
