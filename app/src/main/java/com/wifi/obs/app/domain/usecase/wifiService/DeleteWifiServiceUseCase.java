package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.wifi.WifiService;
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
import java.util.Optional;
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

	private final MemberConverter memberConverter;
	private final WifiServiceConverter wifiServiceConverter;

	private final MemberEntitySupporter memberEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, DeleteServiceRequest request) {

		Member member = getMember(memberId);

		List<WifiService> memberServices = getWifiServices(member.getId());

		Optional<WifiService> deleteTargetService = getWifiService(memberServices, request.getSid());

		if (deleteTargetService.isPresent()) {
			deleteDeviceService.execute(deleteTargetService.get());
			wifiServiceRepository.deleteById(deleteTargetService.get().getId());
		} else {
			throw new NotMatchInformationException();
		}
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private List<WifiService> getWifiServices(Long mid) {
		MemberEntity member = memberEntitySupporter.getReferenceEntity(mid);
		List<WifiServiceEntity> memberActiveServices =
				wifiServiceRepository.findAllByMemberAndDeletedFalse(member);
		return new ArrayList<>(wifiServiceConverter.from(memberActiveServices));
	}

	private Optional<WifiService> getWifiService(List<WifiService> services, Long targetId) {
		return services.stream().filter(service -> service.isSameWifiService(targetId)).findFirst();
	}
}
