package com.wifi.obs.app.domain.usecase.member;

import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.service.device.DeleteDeviceService;
import com.wifi.obs.app.domain.service.wifi.BrowseWifiServiceService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiAuthService;
import com.wifi.obs.app.domain.service.wifi.DeleteWifiServiceService;
import com.wifi.obs.app.exception.domain.MemberNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.support.MemberEntitySupporter;
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

	private final MemberModelConverter memberModelConverter;

	private final MemberEntitySupporter memberEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId) {
		MemberModel member = memberModelConverter.from(getMember(memberId));

		List<WifiServiceEntity> services =
				browseWifiServiceService.execute(memberEntitySupporter.getReferenceEntity(member.getId()));
		List<WifiAuthEntity> auths = getAuths(services);

		deleteWifiAuthService.execute(auths);
		deleteDeviceService.execute(services);
		deleteWifiServiceService.execute(services);

		memberRepository.deleteById(memberId);
	}

	private List<WifiAuthEntity> getAuths(List<WifiServiceEntity> services) {
		return services.stream().map(WifiServiceEntity::getWifiAuthEntity).collect(Collectors.toList());
	}

	private MemberEntity getMember(Long memberId) {
		return memberRepository
				.findByIdAndDeletedFalse(memberId)
				.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}
