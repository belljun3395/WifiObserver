package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.web.dto.request.device.SaveDeviceRequest;
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
public class SaveDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedWifiServiceService validatedWifiServiceService;
	private final ValidatedMemberService validatedMemberService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, SaveDeviceRequest request) {

		WifiServiceEntity service = validatedWifiServiceService.execute(request.getSid());
		MemberEntity member = validatedMemberService.execute(memberId);

		if (!memberId.equals(service.getMember().getId())) {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}

		validateServiceDeviceCount(service, member.getStatus().getMaxDeviceCount());
		validateRequestMacDuplication(request.getMac().toUpperCase(), service);

		deviceRepository.save(
				DeviceEntity.builder()
						.type(request.getDeviceType())
						.mac(request.getMac().toUpperCase())
						.wifiService(service)
						.build());
	}

	private void validateServiceDeviceCount(WifiServiceEntity service, Long maxDeviceCount) {
		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (savedDeviceCount >= maxDeviceCount) {
			throw new RuntimeException("서비스에 더 이상 기기를 추가할 수 없습니다.");
		}
	}

	private void validateRequestMacDuplication(String mac, WifiServiceEntity service) {
		Boolean isExist = deviceRepository.existsByMacAndWifiServiceAndDeletedFalse(mac, service);
		if (isExist) {
			throw new RuntimeException("이미 등록된 기기입니다.");
		}
	}
}
