package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.service.device.DeleteDeviceService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.web.dto.request.service.DeleteServiceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.support.MemberEntitySupporter;
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

	private final MemberModelConverter memberModelConverter;

	private final MemberEntitySupporter memberEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, DeleteServiceRequest request) {

		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		List<WifiServiceEntity> services =
				getServices(memberEntitySupporter.getReferenceEntity(member.getId()));

		if (validateRequestServiceId(request.getSid(), services)) {
			WifiServiceEntity service = getServices(request, services);

			deleteDeviceService.execute(service);

			wifiServiceRepository.deleteById(service.getId());
		} else {
			throw new NotMatchInformationException();
		}
	}

	private WifiServiceEntity getServices(
			DeleteServiceRequest request, List<WifiServiceEntity> services) {
		return Objects.requireNonNull(
				services.stream().filter(s -> s.getId().equals(request.getSid())).findFirst().orElse(null),
				"죄송합니다. 서비스가 존재하지 않습니다.");
	}

	private List<WifiServiceEntity> getServices(MemberEntity member) {
		return new ArrayList<>(wifiServiceRepository.findAllByMemberAndDeletedFalse(member));
	}

	private boolean validateRequestServiceId(Long sid, List<WifiServiceEntity> services) {
		return services.stream()
				.map(WifiServiceEntity::getId)
				.collect(Collectors.toList())
				.contains(sid);
	}
}
