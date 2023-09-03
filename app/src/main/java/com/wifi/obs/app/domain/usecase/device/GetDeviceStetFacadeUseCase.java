package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDeviceStetFacadeUseCase {

	private final DeviceRepository deviceRepository;
	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;

	private final ValidatedMemberService validatedMemberService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceStetInfo execute(Long memberId, String mac, StetType type) {
		MemberEntity member = validatedMemberService.execute(memberId);

		DeviceEntity device =
				deviceRepository
						.findByMacAndDeletedFalse(mac)
						.orElseThrow(() -> new RuntimeException("해당 디바이스가 존재하지 않습니다."));

		if (!member.getId().equals(device.getWifiService().getMember().getId())) {
			throw new RuntimeException("해당 디바이스는 회원의 디바이스가 아닙니다.");
		}

		LocalDateTime now = LocalDateTime.now();

		if (type.equals(StetType.MONTH)) {
			return getMonthDeviceStetInfo(device, now);
		}

		if (type.equals(StetType.DAY)) {
			return getDayDeviceStetInfo(device, now);
		}

		throw new RuntimeException("잘못된 요청입니다.");
	}

	private DeviceStetInfo getMonthDeviceStetInfo(DeviceEntity device, LocalDateTime now) {
		Optional<ConnectHistoryMetaEntity> monthStet =
				connectHistoryMetaRepository.findTopByDeviceAndMonthOrderByIdDesc(
						device, (long) now.getMonth().getValue());

		if (monthStet.isEmpty()) {
			return getDeviceStetInfo(device, Optional.empty());
		}
		return getDeviceStetInfo(device, Optional.of(monthStet.get().getConnectedTimeOnMonth()));
	}

	private DeviceStetInfo getDayDeviceStetInfo(DeviceEntity device, LocalDateTime now) {
		Optional<ConnectHistoryMetaEntity> dayStet =
				connectHistoryMetaRepository.findTopByDeviceAndDayAndMonthOrderByIdDesc(
						device, (long) now.getDayOfMonth(), (long) now.getMonth().getValue());

		if (dayStet.isEmpty()) {
			return getDeviceStetInfo(device, Optional.empty());
		}
		return getDeviceStetInfo(device, Optional.of(dayStet.get().getConnectedTimeOnDay()));
	}

	private DeviceStetInfo getDeviceStetInfo(DeviceEntity device, Optional<Long> connectedTime) {

		if (connectedTime.isEmpty()) {
			return DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
		}

		return DeviceStetInfo.builder()
				.id(device.getId())
				.mac(device.getMac())
				.time(connectedTime.get())
				.build();
	}
}
