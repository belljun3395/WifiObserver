package com.wifi.obs.app.domain.usecase.wifiService;

import static org.assertj.core.api.Assertions.assertThat;

import com.wifi.obs.app.config.AppConfig;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
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
class GetUsersFacadeUseCaseTest {

	@Autowired GetUsersFacadeUseCase getUsersFacadeUseCase;

	static Long deviceId = 1L;
	static String mac = "00:00:00:00:00:00";

	@Test
	void getFilteredRes_userService_O_deviceService_O() {
		// given
		OnConnectUserInfos userInfos =
				OnConnectUserInfos.of(List.of(UserInfo.builder().mac(mac).build()));
		List<DeviceEntity> devices = List.of(DeviceEntity.builder().id(deviceId).mac(mac).build());

		// when
		OnConnectUserInfos res = getUsersFacadeUseCase.getFilteredRes(userInfos, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getUserInfos()).isNotEmpty();
		assertThat(res.getUserInfos().get(0).getMac()).isEqualTo(mac);
	}

	@Test
	void getFilteredRes_userService_O_deviceService_X() {
		// given
		OnConnectUserInfos userInfos =
				OnConnectUserInfos.of(List.of(UserInfo.builder().mac(mac).build()));
		List<DeviceEntity> devices = List.of();

		// when
		OnConnectUserInfos res = getUsersFacadeUseCase.getFilteredRes(userInfos, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getUserInfos()).isEmpty();
	}

	@Test
	void getFilteredRes_userService_X_deviceService_O() {
		// given
		OnConnectUserInfos userInfos = OnConnectUserInfos.of(List.of());
		List<DeviceEntity> devices = List.of(DeviceEntity.builder().id(deviceId).mac(mac).build());

		// when
		OnConnectUserInfos res = getUsersFacadeUseCase.getFilteredRes(userInfos, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getUserInfos()).isEmpty();
	}

	@Test
	void getFilteredRes_userService_X_deviceService_X() {
		// given
		OnConnectUserInfos userInfos = OnConnectUserInfos.of(List.of());
		List<DeviceEntity> devices = List.of();

		// when
		OnConnectUserInfos res = getUsersFacadeUseCase.getFilteredRes(userInfos, devices);

		// then
		assertThat(res).isNotNull();
		assertThat(res.getUserInfos()).isEmpty();
	}
}
