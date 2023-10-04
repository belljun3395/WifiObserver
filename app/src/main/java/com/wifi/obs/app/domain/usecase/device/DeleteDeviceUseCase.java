package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceDeviceConverter;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.web.dto.request.device.DeleteDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;

	private final MemberConverter memberConverter;
	private final WifiServiceDeviceConverter wifiServiceDeviceConverter;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, DeleteDeviceRequest request) {

		Member member = getMember(memberId);

		WifiServiceDevice device = getDevice(request);

		idMatchValidator.validate(member.getId(), device.getMemberId());

		deviceRepository.deleteById(device.getId());
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private WifiServiceDevice getDevice(DeleteDeviceRequest request) {
		return wifiServiceDeviceConverter.from(
				deviceRepository
						.findById(request.getDeviceId())
						.orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId())));
	}
}
