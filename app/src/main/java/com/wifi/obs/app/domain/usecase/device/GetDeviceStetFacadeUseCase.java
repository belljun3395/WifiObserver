package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfo;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDeviceStetFacadeUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final Map<String, GetServiceDeviceStetInfo> getServiceDeviceStetInfoMap;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceStetInfo execute(Long memberId, String mac, StetType type) {
		MemberEntity member = validatedMemberService.execute(memberId);

		DeviceEntity device =
				deviceRepository
						.findByMacAndDeletedFalse(mac)
						.orElseThrow(() -> new DeviceNotFoundException(mac));

		if (!member.getId().equals(device.getWifiService().getMember().getId())) {
			throw new NotMatchInformationException();
		}

		LocalDateTime now = LocalDateTime.now();

		return getMatchStetTypDeviceStetInfo(type).execute(device, now);
	}

	private GetServiceDeviceStetInfo getMatchStetTypDeviceStetInfo(StetType type) {
		String key =
				getServiceDeviceStetInfoMap.keySet().stream()
						.filter(s -> s.contains(type.getType()))
						.findFirst()
						.orElseThrow(BadTypeRequestException::new);

		return getServiceDeviceStetInfoMap.get(key);
	}
}
