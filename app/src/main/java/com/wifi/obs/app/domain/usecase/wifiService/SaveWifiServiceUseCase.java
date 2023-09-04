package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.GetHealthService;
import com.wifi.obs.app.domain.service.wifi.PostAuthService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
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
import java.util.Map;
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

	private final Map<String, GetHealthService> getHealthServiceMap;
	private final Map<String, PostAuthService> postAuthServiceMap;

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
		HttpStatus requestStatus = getMatchServiceTypeHealthService(type).execute(host);

		if (requestStatus != HttpStatus.OK) {
			throw new ClientProblemException();
		}
	}

	public GetHealthService getMatchServiceTypeHealthService(ServiceType type) {
		String key =
				getHealthServiceMap.keySet().stream()
						.filter(s -> s.contains(type.getType()))
						.findFirst()
						.orElseThrow(BadTypeRequestException::new);

		return getHealthServiceMap.get(key);
	}

	private void validateRequestAuthProcess(SaveServiceRequest request) {
		getMatchServiceTypeAuthService(request.getType())
				.execute(request.getHost(), request.getCertification(), request.getPassword());
	}

	public PostAuthService getMatchServiceTypeAuthService(ServiceType type) {
		String key =
				postAuthServiceMap.keySet().stream()
						.filter(s -> s.contains(type.getType()))
						.findFirst()
						.orElseThrow(BadTypeRequestException::new);

		return postAuthServiceMap.get(key);
	}
}
