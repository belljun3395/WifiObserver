package com.wifi.obs.app.domain.usecase.wifiService;

import static org.assertj.core.api.Assertions.assertThat;

import com.wifi.obs.app.config.AppConfig;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.model.ModelId;
import com.wifi.obs.app.domain.model.device.Device;
import com.wifi.obs.app.domain.model.device.DeviceType;
import com.wifi.obs.app.domain.model.wifi.WifiAuth;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
import com.wifi.obs.app.domain.model.wifi.WifiStatus;
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
	static WifiAuth auth =
			WifiAuth.builder()
					.id(ModelId.of(1L))
					.host("host")
					.certification("certification")
					.password("password")
					.build();

	static Long deviceId = 1L;
	static String mac = "00:00:00:00:00:00";

	@Test
	void getServiceInfos() {
		// given
		List<WifiService> services =
				List.of(
						WifiService.builder()
								.id(ModelId.of(serviceId))
								.memberId(ModelId.of(memberId))
								.auth(auth)
								.type(WifiServiceType.IPTIME)
								.cycle(10L)
								.standardTime(9L)
								.status(WifiStatus.ON)
								.createdAt(LocalDateTime.now())
								.build());

		List<List<Device>> devices =
				List.of(
						List.of(
								Device.builder()
										.id(ModelId.of(deviceId))
										.serviceId(ModelId.of(serviceId))
										.type(DeviceType.NOTEBOOK)
										.mac(mac)
										.build()));

		// when
		WifiServiceInfos res = getWifiServiceInfoUseCase.getServiceInfos(services, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getServices()).isNotEmpty();
		assertThat(res.getServices().get(0).getDevices().get(0).getId()).isEqualTo(deviceId);
		assertThat(res.getServices().get(0).getDevices().get(0).getMac()).isEqualTo(mac);
		assertThat(res.getServices().get(0).getDevices().get(0).getType())
				.isEqualTo(DeviceType.NOTEBOOK);
	}
}
