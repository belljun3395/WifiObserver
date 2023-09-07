package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.usecase.support.manager.GetHealthServiceManager;
import com.wifi.obs.app.domain.usecase.support.manager.PostAuthServiceManager;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.IncludeNotAllowHostNumberException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.service.SaveServiceRequest;
import com.wifi.obs.app.web.dto.request.service.ServiceType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.repository.wifi.auth.WifiAuthRepository;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveWifiServiceUseCase {
	private static final List<String> NOT_ALLOW_HOST_FIRST_PART = List.of("192", "10", "172");

	private final WifiServiceRepository wifiServiceRepository;
	private final WifiAuthRepository wifiAuthRepository;

	private final ValidatedMemberService validatedMemberService;
	private final GetHealthServiceManager getHealthServiceManager;
	private final PostAuthServiceManager postAuthServiceManager;

	private final MemberModelConverter memberModelConverter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, SaveServiceRequest request) {

		validateMember(memberId);
		validateRequest(request);

		WifiAuthEntity auth =
				wifiAuthRepository.save(
						WifiAuthEntity.builder()
								.host(request.getHost())
								.certification(request.getCertification())
								.password(request.getPassword())
								.build());

		wifiServiceRepository.save(
				WifiServiceEntity.builder()
						.serviceType(WifiServiceType.valueOf(request.getType().getName()))
						.cycle(request.getCycle())
						.member(MemberEntity.builder().id(memberId).build())
						.wifiAuthEntity(auth)
						.build());
	}

	private void validateMember(Long memberId) {
		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		validateMemberServiceCount(member);
	}

	private void validateMemberServiceCount(MemberModel member) {

		List<WifiServiceEntity> services =
				wifiServiceRepository.findAllByMemberAndDeletedFalse(
						MemberEntity.builder().id(member.getId()).build());

		if (member.isOverServiceMaxCount((long) services.size())) {
			throw new OverLimitException();
		}
	}

	private void validateRequest(SaveServiceRequest request) {
		validateHostForm(request.getHost());
		validateHostRequestStatus(request.getType(), request.getHost());
		validateRequestAuthProcess(request);
	}

	private void validateHostForm(String host) {
		String hostFirstPart = StringUtils.substringBefore(host, ".");

		if (NOT_ALLOW_HOST_FIRST_PART.contains(hostFirstPart)) {
			throw new IncludeNotAllowHostNumberException(hostFirstPart);
		}
	}

	private void validateHostRequestStatus(ServiceType type, String host) {
		HttpStatus requestStatus = getHealthServiceManager.getService(type).execute(host);

		if (requestStatus != HttpStatus.OK) {
			throw new ClientProblemException();
		}
	}

	private void validateRequestAuthProcess(SaveServiceRequest request) {
		postAuthServiceManager
				.getService(request.getType())
				.execute(request.getHost(), request.getCertification(), request.getPassword());
	}
}
