package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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
	private final ValidatedWifiServiceService validatedWifiServiceService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, PatchDeviceRequest request) {

		MemberEntity member = validatedMemberService.execute(memberId);

		DeviceEntity device =
				deviceRepository
						.findById(request.getDeviceId())
						.orElseThrow(() -> new RuntimeException("존재하지 않는 기기입니다."));

		if (request.getChangeServiceId().equals(device.getWifiService().getId())) {
			return;
		}

		WifiServiceEntity changeTargetService =
				validatedWifiServiceService.execute(request.getChangeServiceId());

		if (memberId.equals(changeTargetService.getMember().getId())) {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}

		validateServiceDeviceCount(changeTargetService, member.getStatus().getMaxDeviceCount());

		deviceRepository.save(device.toBuilder().wifiService(changeTargetService).build());
	}

	private void validateServiceDeviceCount(WifiServiceEntity service, Long maxDeviceCount) {
		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (savedDeviceCount >= maxDeviceCount) {
			throw new RuntimeException("서비스에 더 이상 기기를 추가할 수 없습니다.");
		}
	}
}
