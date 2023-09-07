package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfos;
import com.wifi.obs.app.domain.dto.response.device.ServiceDeviceInfo;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServicesService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
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
public class GetServiceDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedWifiServiceService validatedWifiServiceService;
	private final ValidatedWifiServicesService validatedWifiServicesService;
	private final ValidatedMemberService validatedMemberService;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceInfos execute(Long memberId, Long serviceId) {

		WifiServiceEntity service = validatedWifiServiceService.execute(serviceId);

		idMatchValidator.validate(memberId, service.getMember().getId());

		return DeviceInfos.of(getDeviceInfos(service));
	}

	private List<DeviceInfo> getDeviceInfos(WifiServiceEntity service) {
		return deviceRepository.findAllByWifiServiceAndDeletedFalse(service).stream()
				.map(d -> DeviceInfo.builder().id(d.getId()).type(d.getType()).mac(d.getMac()).build())
				.collect(Collectors.toList());
	}

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceInfos execute(Long memberId) {

		MemberEntity member = validatedMemberService.execute(memberId);

		List<WifiServiceEntity> services = validatedWifiServicesService.execute(member);

		return DeviceInfos.of(getDeviceInfos(services));
	}

	private List<ServiceDeviceInfo> getDeviceInfos(List<WifiServiceEntity> services) {
		List<ServiceDeviceInfo> devices = new ArrayList<>();
		for (WifiServiceEntity service : services) {
			devices.addAll(getServiceDeviceInfo(service));
		}
		return devices;
	}

	private List<ServiceDeviceInfo> getServiceDeviceInfo(WifiServiceEntity service) {
		return deviceRepository.findAllByWifiServiceAndDeletedFalse(service).stream()
				.map(
						device ->
								ServiceDeviceInfo.builder()
										.id(device.getId())
										.serviceId(service.getId())
										.type(device.getType())
										.mac(device.getMac())
										.build())
				.collect(Collectors.toList());
	}
}
