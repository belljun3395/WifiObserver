package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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
		List<WifiServiceEntity> wifiServices =
				wifiServiceRepository.findAllByMemberAndDeletedFalse(
						MemberEntity.builder().id(memberId).build());

		List<List<DeviceEntity>> devices =
				wifiServices.stream().map(browseDeviceService::execute).collect(Collectors.toList());

		return getWifiServiceInfos(wifiServices, devices);
	}

	private WifiServiceInfos getWifiServiceInfos(
			List<WifiServiceEntity> wifiServices, List<List<DeviceEntity>> devices) {

		List<WifiServiceInfo> serviceInfos = new ArrayList<>();

		for (int i = 0; i < wifiServices.size(); i++) {
			WifiServiceEntity cws = wifiServices.get(i);
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
							.createAt(cws.getCreateAt().toString())
							.deviceInfos(deviceInfos)
							.build());
		}

		return new WifiServiceInfos(serviceInfos);
	}
}
