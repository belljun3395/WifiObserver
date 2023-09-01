package com.wifi.obs.app.domain.usecase.member;

import com.wifi.obs.app.domain.service.device.DeleteDeviceService;
import com.wifi.obs.app.domain.service.wifi.BrowseWifiServiceService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiAuthService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiServiceService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.member.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMemberUseCase {

	private final MemberRepository memberRepository;

	private final BrowseWifiServiceService browseWifiServiceService;
	private final DeleteWifiAuthService deleteWifiAuthService;
	private final DeleteDeviceService deleteDeviceService;
	private final DeleteWifiServiceService deleteWifiServiceService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId) {
		MemberEntity member =
				memberRepository
						.findById(memberId)
						.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		List<WifiServiceEntity> services = browseWifiServiceService.execute(member);
		List<WifiAuthEntity> auths =
				services.stream().map(WifiServiceEntity::getWifiAuthEntity).collect(Collectors.toList());

		deleteWifiAuthService.execute(auths);
		deleteDeviceService.execute(services);
		deleteWifiServiceService.execute(services);

		memberRepository.deleteById(memberId);
	}
}
