package com.wifi.obs.app.domain.usecase.member;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.device.DeleteDeviceService;
import com.wifi.obs.app.domain.service.wifi.BrowseWifiServiceService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiAuthService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiServiceService;
import com.wifi.obs.app.exception.domain.MemberNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.MemberEntitySupporter;
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

	private final MemberConverter memberConverter;
	private final WifiServiceConverter wifiServiceConverter;

	private final MemberEntitySupporter memberEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId) {

		Member member = getMember(memberId);

		List<WifiService> memberServices = getWifiServices(member);

		List<Long> auths = getAuths(memberServices);

		deleteWifiAuthService.execute(auths);
		deleteDeviceService.execute(memberServices);
		deleteWifiServiceService.execute(memberServices);

		memberRepository.deleteById(memberId);
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(
				memberRepository
						.findByIdAndDeletedFalse(memberId)
						.orElseThrow(() -> new MemberNotFoundException(memberId)));
	}

	private List<WifiService> getWifiServices(Member member) {
		return wifiServiceConverter.from(
				browseWifiServiceService.execute(memberEntitySupporter.getReferenceEntity(member.getId())));
	}

	private List<Long> getAuths(List<WifiService> services) {
		return services.stream().map(WifiService::getAuthId).collect(Collectors.toList());
	}
}
