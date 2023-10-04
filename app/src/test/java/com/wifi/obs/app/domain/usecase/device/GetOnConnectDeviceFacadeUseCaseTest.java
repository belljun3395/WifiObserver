package com.wifi.obs.app.domain.usecase.device;

import static org.assertj.core.api.Assertions.assertThat;

import com.wifi.obs.app.config.AppConfig;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import java.util.List;
import java.util.Optional;
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
class GetOnConnectDeviceFacadeUseCaseTest {

	@Autowired GetOnConnectDeviceFacadeUseCase getOnConnectDeviceFacadeUseCase;

	static String mac = "00:00:00:00:00:00";

	@Test
	void getDeviceOnConnectInfo_on_connect() {
		// given
		OnConnectUserInfos connectUserInfos =
				OnConnectUserInfos.of(List.of(UserInfo.builder().mac(mac).build()));

		// when
		Optional<String> res = getOnConnectDeviceFacadeUseCase.getFilteredUsers(mac, connectUserInfos);

		// then
		assertThat(res).isPresent();
		assertThat(res.get()).contains(mac);
	}

	@Test
	void getDeviceOnConnectInfo_disconnect() {
		// given
		OnConnectUserInfos connectUserInfos = OnConnectUserInfos.of(List.of());

		// when
		Optional<String> res = getOnConnectDeviceFacadeUseCase.getFilteredUsers(mac, connectUserInfos);

		// then
		assertThat(res).isEmpty();
	}
}
