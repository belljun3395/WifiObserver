package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceConverter;
import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceDeviceConverter;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatchDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;

	private final MemberConverter memberConverter;
	private final DeviceConverter deviceConverter;
	private final WifiServiceDeviceConverter wifiServiceDeviceConverter;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, PatchDeviceRequest request) {

		Member member = getMember(memberId);

		WifiServiceDevice device = getDevice(request.getDeviceId());

		WifiService changeTargetService = device.getService();

		validate(changeTargetService, device, memberId, request.getChangeServiceId());

		checkConstraint(member, changeTargetService.getId());

		device.changeService(changeTargetService.getId());

		deviceRepository.save(deviceConverter.toEntity(device));
	}

	private void validate(
			WifiService changeTargetService,
			WifiServiceDevice device,
			Long memberId,
			Long changeServiceId) {
		validateService(changeTargetService, memberId);

		validateDevice(device, changeServiceId);
	}

	private void validateService(WifiService changeTargetService, Long memberId) {
		if (changeTargetService.isServiceOwner(memberId)) {
			throw new NotMatchInformationException();
		}
	}

	private void validateDevice(WifiServiceDevice device, Long changeServiceId) {
		if (device.isSameService(changeServiceId)) {
			return;
		}
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private WifiServiceDevice getDevice(Long deviceId) {
		return wifiServiceDeviceConverter.from(
				deviceRepository
						.findByIdAndDeletedFalse(deviceId)
						.orElseThrow(() -> new DeviceNotFoundException(deviceId)));
	}

	private void checkConstraint(Member member, Long serviceId) {
		if (member.isOverDeviceMax(getSavedDeviceCount(serviceId))) {
			throw new OverLimitException();
		}
	}

	private long getSavedDeviceCount(Long serviceId) {
		return deviceRepository
				.findAllByWifiServiceAndDeletedFalse(
						wifiServiceEntitySupporter.getReferenceEntity(serviceId))
				.size();
	}
}
