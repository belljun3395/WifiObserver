package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.iptime.GetIptimeHealthService;
import com.wifi.obs.app.domain.service.wifi.iptime.PostIptimeAuthService;
import com.wifi.obs.app.web.dto.request.service.SaveServiceRequest;
import com.wifi.obs.app.web.dto.request.service.ServiceType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.repository.wifi.auth.WifiAuthRepository;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import java.util.List;
import java.util.Optional;
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

	private final GetIptimeHealthService getIptimeHealthService;
	private final PostIptimeAuthService postIptimeAuthService;

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
		MemberEntity member = validatedMemberService.execute(memberId);

		validateMemberServiceCount(memberId, member.getStatus().getMaxServiceCount());
	}

	private void validateMemberServiceCount(Long memberId, Long maxServiceCount) {

		List<WifiServiceEntity> services =
				wifiServiceRepository.findAllByMemberAndDeletedFalse(
						MemberEntity.builder().id(memberId).build());

		if (services.size() >= maxServiceCount) {
			throw new RuntimeException("더 이상 서비스를 신청할 수 없습니다.");
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
			throw new RuntimeException("해당 주소는 사용할 수 없습니다.");
		}
	}

	private void validateHostRequestStatus(ServiceType type, String host) {
		if (type.equals(ServiceType.IPTIME)) {
			HttpStatus requestStatus = getIptimeHealthService.execute(host);

			if (requestStatus != HttpStatus.OK) {
				throw new RuntimeException("해당 주소에 접속할 수 없습니다.");
			}
		}
	}

	private void validateRequestAuthProcess(SaveServiceRequest request) {
		if (request.getType().equals(ServiceType.IPTIME)) {
			Optional<AuthInfo> authInfo =
					postIptimeAuthService.execute(
							request.getHost(), request.getCertification(), request.getPassword());

			if (authInfo.isEmpty()) {
				throw new RuntimeException("인증에 실패하였습니다.");
			}
		}
	}
}
