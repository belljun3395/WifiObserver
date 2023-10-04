package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfos;
import com.wifi.obs.app.domain.dto.response.device.ServiceDeviceInfo;
import com.wifi.obs.app.domain.model.device.DeviceType;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServicesService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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

	private final WifiServiceConverter wifiServiceConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceInfos execute(Long memberId, Long serviceId) {

		WifiService service = getWifiService(serviceId);

		idMatchValidator.validate(memberId, service.getMemberId());

		List<DeviceInfo> serviceDevices = getDevices(service.getId());

		return DeviceInfos.of(serviceDevices);
	}

	private WifiService getWifiService(Long serviceId) {
		return wifiServiceConverter.from(validatedWifiServiceService.execute(serviceId));
	}

	private List<DeviceInfo> getDevices(Long sid) {
		return deviceRepository
				.findAllByWifiServiceAndDeletedFalse(wifiServiceEntitySupporter.getReferenceEntity(sid))
				.stream()
				.map(this::getDeviceInfo)
				.collect(Collectors.toList());
	}

	private DeviceInfo getDeviceInfo(DeviceEntity d) {
		return DeviceInfo.builder()
				.id(d.getId())
				.type(DeviceType.valueOf(d.getType().getType()))
				.mac(d.getMac())
				.build();
	}

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceInfos execute(Long memberId) {

		List<WifiService> memberServices = getWifiServices(memberId);

		List<ServiceDeviceInfo> memberDevices = getDevices(memberServices);

		return DeviceInfos.of(memberDevices);
	}

	private List<WifiService> getWifiServices(Long memberId) {
		return wifiServiceConverter.from(
				validatedWifiServicesService.execute(validatedMemberService.execute(memberId)));
	}

	private List<ServiceDeviceInfo> getDevices(List<WifiService> services) {
		List<ServiceDeviceInfo> devices = new ArrayList<>();
		for (WifiService service : services) {
			devices.addAll(getServiceDeviceInfos(service));
		}
		return devices;
	}

	private List<ServiceDeviceInfo> getServiceDeviceInfos(WifiService service) {
		return deviceRepository
				.findAllByWifiServiceAndDeletedFalse(
						wifiServiceEntitySupporter.getWifiServiceIdEntity(service.getId()))
				.stream()
				.map(convertToDeviceInfo(service))
				.collect(Collectors.toList());
	}

	private Function<DeviceEntity, ServiceDeviceInfo> convertToDeviceInfo(WifiService service) {
		return source ->
				ServiceDeviceInfo.builder()
						.serviceId(service.getId())
						.id(source.getId())
						.type(DeviceType.valueOf(source.getType().getType()))
						.mac(source.getMac())
						.build();
	}
}
