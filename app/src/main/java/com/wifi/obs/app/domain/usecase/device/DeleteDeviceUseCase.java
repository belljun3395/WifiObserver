package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
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

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, DeleteDeviceRequest request) {

		validatedMemberService.execute(memberId);

		deviceRepository.deleteById(request.getDeviceId());
	}
}
