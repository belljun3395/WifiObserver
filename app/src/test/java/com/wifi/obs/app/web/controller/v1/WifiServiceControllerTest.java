package com.wifi.obs.app.web.controller.v1;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifi.obs.app.AppMain;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfo;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.usecase.wifiService.DeleteWifiServiceUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetHealthStatusUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetServiceStetFacadeUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetUsersFacadeUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetWifiServiceInfoUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.SaveWifiServiceUseCase;
import com.wifi.obs.app.web.controller.v1.description.Description;
import com.wifi.obs.app.web.controller.v1.description.ServiceDescription;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.app.web.dto.request.service.DeleteServiceRequest;
import com.wifi.obs.app.web.dto.request.service.SaveServiceRequest;
import com.wifi.obs.app.web.dto.request.service.ServiceType;
import com.wifi.obs.data.mysql.entity.device.DeviceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = AppMain.class)
class WifiServiceControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private SaveWifiServiceUseCase saveWifiServiceUseCase;
	@MockBean private DeleteWifiServiceUseCase deleteWifiServiceUseCase;
	@MockBean private GetWifiServiceInfoUseCase getWifiServiceInfoUseCase;
	@MockBean private GetUsersFacadeUseCase getUsersFacadeUseCase;
	@MockBean private GetHealthStatusUseCase getHealthStatusUseCase;
	@MockBean private GetServiceStetFacadeUseCase getServiceStetFacadeUseCase;

	private static final String TAG = "WifiService";
	private static final String BASE_URL = "/api/v1/wifi";

	@Test
	void saveDevice() throws Exception {
		SaveServiceRequest request =
				SaveServiceRequest.builder()
						.type(ServiceType.IPTIME)
						.cycle(10L)
						.host("192.168.0.1")
						.certification("id")
						.password("password")
						.build();

		String content =
				"{\"type\":\"ip\",\"cycle\":10,\"host\":\"192.168.0.1\",\"certification\":\"id\",\"password\":\"password\"}";

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.header("Authorization", "{{accessToken}}")
								.content(content)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveService",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 등록")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveServiceRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("SaveServiceResponse"))
												.responseFields(Description.success(ServiceDescription.saveService()))
												.build())));
	}

	@Test
	void deleteDevice() throws Exception {
		DeleteServiceRequest request = DeleteServiceRequest.builder().sid(1L).build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						delete(BASE_URL, 0)
								.header("Authorization", "{{accessToken}}")
								.content(content)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeleteService",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 해지")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteServiceRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("DeleteServiceResponse"))
												.responseFields(Description.success(ServiceDescription.deleteService()))
												.build())));
	}

	@Test
	void broseServiceInfo() throws Exception {

		WifiServiceInfo info =
				WifiServiceInfo.builder()
						.id(1L)
						.type(WifiServiceType.IPTIME)
						.cycle(10L)
						.status(WifiStatus.ON)
						.createAt("시간")
						.deviceInfos(
								List.of(
										DeviceInfo.builder()
												.id(1L)
												.type(DeviceType.NOTEBOOK)
												.mac("00:00:00:00:00:00")
												.build(),
										DeviceInfo.builder()
												.id(1L)
												.type(DeviceType.NOTEBOOK)
												.mac("00:00:00:00:00:00")
												.build()))
						.build();
		WifiServiceInfos res = new WifiServiceInfos(List.of(info, info));
		when(getWifiServiceInfoUseCase.execute(anyLong())).thenReturn(res);
		mockMvc
				.perform(
						get(BASE_URL + "/service", 0)
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"BroseServiceInfo",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 정보 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseServiceInfoRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("BrowseServiceInfoResponse"))
												.responseFields(
														Description.success(ServiceDescription.browseServiceInfos()))
												.build())));
	}

	@Test
	void browseUsers() throws Exception {

		UserInfo info = UserInfo.builder().mac("00:00:00:00:00:00").build();
		OnConnectUserInfos res = new OnConnectUserInfos(List.of(info, info));

		when(getUsersFacadeUseCase.execute(anyLong(), anyLong(), any())).thenReturn(res);

		mockMvc
				.perform(
						get(BASE_URL + "/users", 2)
								.param("sid", "1")
								.param("ft", "true")
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"BroseUsers",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 유저 정보 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseUsersRequest"))
												.requestParameters(
														parameterWithName("sid").description("서비스 아이디"),
														parameterWithName("ft").description("등록 기기 필터링").optional())
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("BrowseUsersResponse"))
												.responseFields(Description.success(ServiceDescription.browseUsers()))
												.build())));
	}

	@Test
	void health() throws Exception {

		when(getHealthStatusUseCase.execute(anyLong(), anyLong())).thenReturn(HttpStatus.OK);

		mockMvc
				.perform(
						get(BASE_URL + "/health", 1)
								.param("sid", "1")
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"HealthService",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 상태 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("HealthServiceRequest"))
												.requestParameters(parameterWithName("sid").description("서비스 아이디"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("HealthServiceResponse"))
												.responseFields(Description.success(ServiceDescription.health()))
												.build())));
	}

	@Test
	void serviceStet() throws Exception {

		DeviceStetInfo info = DeviceStetInfo.builder().id(1L).mac("00:00:00:00:00:00").time(1L).build();

		ServiceDeviceStetInfos res = new ServiceDeviceStetInfos(1L, List.of(info, info));

		when(getServiceStetFacadeUseCase.execute(anyLong(), anyLong(), any())).thenReturn(res);

		mockMvc
				.perform(
						get(BASE_URL + "/statistics", 2)
								.param("sid", "1")
								.param("type", StetType.DAY.getCode())
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"ServiceStet",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 통계 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("ServiceStetRequest"))
												.requestParameters(
														parameterWithName("sid").description("서비스 아이디"),
														parameterWithName("type").description("통계 타입"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("ServiceStetResponse"))
												.responseFields(Description.success(ServiceDescription.serviceStet()))
												.build())));
	}
}
