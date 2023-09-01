package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.service.device.DeleteDeviceService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.web.dto.request.service.DeleteServiceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteWifiServiceUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final DeleteDeviceService deleteDeviceService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, DeleteServiceRequest request) {

		MemberEntity member = validatedMemberService.execute(memberId);

		List<WifiServiceEntity> services =
				new ArrayList<>(wifiServiceRepository.findAllByMemberAndDeletedFalse(member));

		if (validateRequestServiceId(request.getSid(), services)) {
			WifiServiceEntity service =
					Objects.requireNonNull(
							services.stream().filter(s -> s.getId().equals(request.getSid())).findFirst().get(),
							"죄송합니다. 서비스가 존재하지 않습니다.");

			deleteDeviceService.execute(service);

			wifiServiceRepository.deleteById(service.getId());
		} else {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}
	}

	private boolean validateRequestServiceId(Long wifiServiceId, List<WifiServiceEntity> services) {
		return services.stream()
				.map(WifiServiceEntity::getId)
				.collect(Collectors.toList())
				.contains(wifiServiceId);
	}
}
