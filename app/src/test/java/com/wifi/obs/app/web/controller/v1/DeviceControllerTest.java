package com.wifi.obs.app.web.controller.v1;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifi.obs.app.AppMain;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceInfos;
import com.wifi.obs.app.domain.dto.response.device.DeviceOnConnectInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.usecase.device.DeleteDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.GetDeviceStetFacadeUseCase;
import com.wifi.obs.app.domain.usecase.device.GetOnConnectDeviceFacadeUseCase;
import com.wifi.obs.app.domain.usecase.device.GetServiceDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.PatchDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.SaveDeviceUseCase;
import com.wifi.obs.app.web.controller.v1.description.Description;
import com.wifi.obs.app.web.controller.v1.description.DeviceDescription;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.app.web.dto.request.device.DeleteDeviceRequest;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
import com.wifi.obs.app.web.dto.request.device.SaveDeviceRequest;
import com.wifi.obs.data.mysql.entity.device.DeviceType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = AppMain.class)
class DeviceControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private SaveDeviceUseCase saveDeviceUseCase;
	@MockBean private PatchDeviceUseCase patchDeviceUseCase;
	@MockBean private DeleteDeviceUseCase deleteDeviceUseCase;
	@MockBean private GetServiceDeviceUseCase getServiceDeviceUseCase;
	@MockBean private GetOnConnectDeviceFacadeUseCase getOnConnectDeviceFacadeUseCase;
	@MockBean private GetDeviceStetFacadeUseCase getDeviceStetFacadeUseCase;

	private static final String TAG = "Device";
	private static final String BASE_URL = "/api/v1/device";

	@Test
	void saveDevice() throws Exception {
		SaveDeviceRequest request =
				SaveDeviceRequest.builder()
						.sid(1L)
						.deviceType(DeviceType.NOTEBOOK)
						.mac("00:00:00:00:00:00")
						.build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.header("Authorization", "{{accessToken}}")
								.content(content)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("장치 등록")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveDeviceRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("SaveDeviceResponse"))
												.responseFields(Description.success(DeviceDescription.saveDevice()))
												.build())));
	}

	@Test
	void editDevice() throws Exception {
		PatchDeviceRequest request =
				PatchDeviceRequest.builder().deviceId(1L).changeServiceId(2L).build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						patch(BASE_URL, 0)
								.header("Authorization", "{{accessToken}}")
								.content(content)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"EditDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("장치 수정")
												.tag(TAG)
												.requestSchema(Schema.schema("EditDeviceRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("EditDeviceResponse"))
												.responseFields(Description.success(DeviceDescription.editDevice()))
												.build())));
	}

	@Test
	void deleteDevice() throws Exception {
		DeleteDeviceRequest request = DeleteDeviceRequest.builder().deviceId(1L).build();

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
								"DeleteDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("장치 삭제")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteDeviceRequest"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("DeleteDeviceResponse"))
												.responseFields(Description.success(DeviceDescription.deleteDevice()))
												.build())));
	}

	@Test
	void browseServiceDevice() throws Exception {

		DeviceInfo info =
				DeviceInfo.builder().id(1L).type(DeviceType.NOTEBOOK).mac("00:00:00:00:00:00").build();
		DeviceInfos res = new DeviceInfos(List.of(info));

		when(getServiceDeviceUseCase.execute(anyLong(), anyLong())).thenReturn(res);

		mockMvc
				.perform(
						get(BASE_URL + "/service/{serviceId}", 1)
								.param("serviceId", "1")
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"BrowseServiceDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("서비스 장치 조회")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseServiceDeviceRequest"))
												.pathParameters(parameterWithName("serviceId").description("서비스 id"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("BrowseServiceDeviceResponse"))
												.responseFields(
														Description.success(DeviceDescription.browseServiceDevice()))
												.build())));
	}

	@Test
	void isConnect() throws Exception {

		DeviceOnConnectInfo res =
				DeviceOnConnectInfo.builder().id(1L).mac("00:00:00:00:00:00").connected(true).build();
		when(getOnConnectDeviceFacadeUseCase.execute(anyLong(), anyString())).thenReturn(res);

		mockMvc
				.perform(
						get(BASE_URL + "/connect", 1)
								.param("mac", "00:00:00:00:00:00")
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"IsConnectDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("장치 접속 여부")
												.tag(TAG)
												.requestSchema(Schema.schema("IsConnectDeviceRequest"))
												.requestParameters(parameterWithName("mac").description("mac 주소"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("IsConnectDeviceResponse"))
												.responseFields(Description.success(DeviceDescription.isConnectDevice()))
												.build())));
	}

	@Test
	void deviceStet() throws Exception {

		DeviceStetInfo res = DeviceStetInfo.builder().id(1L).mac("00:00:00:00:00:00").time(10L).build();
		when(getDeviceStetFacadeUseCase.execute(anyLong(), anyString(), any(StetType.class)))
				.thenReturn(res);

		mockMvc
				.perform(
						get(BASE_URL + "/statistics", 2)
								.param("mac", "00:00:00:00:00:00")
								.param("type", StetType.DAY.toString())
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeviceStet",
								resource(
										ResourceSnippetParameters.builder()
												.description("장치 통계")
												.tag(TAG)
												.requestSchema(Schema.schema("DeviceStetRequest"))
												.requestParameters(
														parameterWithName("mac").description("mac 주소"),
														parameterWithName("type").description("통계 타입"))
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("DeviceStetResponse"))
												.responseFields(Description.success(DeviceDescription.deviceStet()))
												.build())));
	}
}
