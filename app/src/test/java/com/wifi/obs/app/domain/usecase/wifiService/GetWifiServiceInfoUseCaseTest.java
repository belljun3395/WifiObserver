package com.wifi.obs.app.domain.usecase.wifiService;

import static org.assertj.core.api.Assertions.assertThat;

import com.wifi.obs.app.config.AppConfig;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.model.DeviceModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.data.mysql.entity.device.DeviceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {AppConfig.class})
class GetWifiServiceInfoUseCaseTest {

	@Autowired GetWifiServiceInfoUseCase getWifiServiceInfoUseCase;

	static Long serviceId = 1L;
	static Long memberId = 1L;
	static Long authId = 1L;
	static Long deviceId = 1L;
	static String mac = "00:00:00:00:00:00";

	@Test
	void getServiceInfos() {
		// given
		List<WifiServiceModel> services =
				List.of(
						WifiServiceModel.builder()
								.id(serviceId)
								.memberId(memberId)
								.authId(authId)
								.serviceType(WifiServiceType.IPTIME)
								.cycle(10L)
								.standardTime(9L)
								.status(WifiStatus.ON)
								.createdAt(LocalDateTime.now())
								.build());

		List<List<DeviceModel>> devices =
				List.of(
						List.of(
								DeviceModel.builder()
										.id(deviceId)
										.serviceId(serviceId)
										.deviceType(DeviceType.NOTEBOOK)
										.mac(mac)
										.build()));

		// when
		WifiServiceInfos res = getWifiServiceInfoUseCase.getServiceInfos(services, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getWifiServiceInfos()).isNotEmpty();
		assertThat(res.getWifiServiceInfos().get(0).getDeviceInfos().get(0).getId())
				.isEqualTo(deviceId);
		assertThat(res.getWifiServiceInfos().get(0).getDeviceInfos().get(0).getMac()).isEqualTo(mac);
		assertThat(res.getWifiServiceInfos().get(0).getDeviceInfos().get(0).getType())
				.isEqualTo(DeviceType.NOTEBOOK);
	}
}
