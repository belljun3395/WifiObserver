package com.observer.web.controller.device;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.observer.ApiMain;
import com.observer.domain.dto.device.GetDevicesUseCaseResponse;
import com.observer.domain.dto.device.PostDeviceUseCaseResponse;
import com.observer.domain.usecase.device.DeleteDeviceUseCase;
import com.observer.domain.usecase.device.GetDevicesUseCase;
import com.observer.domain.usecase.device.PostDeviceUseCase;
import com.observer.entity.device.DeviceEntity;
import com.observer.entity.device.DeviceType;
import com.observer.web.controller.config.TestApiKeyUserDetailsService;
import com.observer.web.controller.description.Description;
import com.observer.web.controller.dto.device.DeleteDeviceRequest;
import com.observer.web.controller.dto.device.PostDeviceRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {ApiMain.class, TestApiKeyUserDetailsService.class})
class DeviceControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean PostDeviceUseCase postDeviceUseCase;
	@MockBean DeleteDeviceUseCase deleteDeviceUseCase;
	@MockBean GetDevicesUseCase getDeviceUseCase;

	private static final String TAG = "DeviceController";
	private static final String BASE_URL = "/api/v1/devices";

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void save() throws Exception {
		PostDeviceRequest request =
				PostDeviceRequest.builder()
						.routeId(1L)
						.mac("000.000.000.000:0000")
						.info("{ \"key\" : \"value\"}")
						.build();

		String content = objectMapper.writeValueAsString(request);

		when(postDeviceUseCase.execute(any()))
				.thenReturn(
						PostDeviceUseCaseResponse.builder()
								.deviceId(1L)
								.routeId(1L)
								.mac("000.000.000.000:0000")
								.info("{ \"key\" : \"value\"}")
								.type("type")
								.build());

		mockMvc
				.perform(post(BASE_URL, 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("기기를 등록합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveDeviceRequest"))
												.responseSchema(Schema.schema("SaveDeviceResponse"))
												.responseFields(
														Description.created(
																new FieldDescriptor[] {
																	fieldWithPath("data.deviceId")
																			.type(JsonFieldType.NUMBER)
																			.description("장치 id"),
																	fieldWithPath("data.routeId")
																			.type(JsonFieldType.NUMBER)
																			.description("라우터 id"),
																	fieldWithPath("data.type")
																			.type(JsonFieldType.STRING)
																			.description("장치 타입"),
																	fieldWithPath("data.mac")
																			.type(JsonFieldType.STRING)
																			.description("장치 mac"),
																	fieldWithPath("data.info")
																			.type(JsonFieldType.STRING)
																			.description("장치 정보"),
																}))
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void delete() throws Exception {
		DeleteDeviceRequest request = DeleteDeviceRequest.builder().routeId(1L).deviceId(1L).build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL + "/delete", 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeleteDevice",
								resource(
										ResourceSnippetParameters.builder()
												.description("기기를 삭제합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteDeviceRequest"))
												.responseSchema(Schema.schema("DeleteDeviceResponse"))
												.responseFields(Description.deleted())
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void getRouterDevices() throws Exception {
		List<DeviceEntity> entities = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			entities.add(
					DeviceEntity.builder()
							.id((long) i)
							.type(DeviceType.NOTEBOOK)
							.mac(i + "00.000.000.000:0000")
							.info("{ \"key\" : \"value\"}")
							.build());
		}
		when(getDeviceUseCase.execute(any())).thenReturn(GetDevicesUseCaseResponse.from(entities));

		mockMvc
				.perform(get(BASE_URL + "/routers/{routerId}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"GetRouterDevices",
								resource(
										ResourceSnippetParameters.builder()
												.description("라우터의 기기 목록을 조회합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("GetRouterDevicesRequest"))
												.responseSchema(Schema.schema("GetRouterDevicesResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.devices")
																			.type(JsonFieldType.ARRAY)
																			.description("장치 목록"),
																	fieldWithPath("data.devices[].id")
																			.type(JsonFieldType.NUMBER)
																			.description("장치 id"),
																	fieldWithPath("data.devices[].type")
																			.type(JsonFieldType.STRING)
																			.description("장치 타입"),
																	fieldWithPath("data.devices[].mac")
																			.type(JsonFieldType.STRING)
																			.description("장치 mac"),
																	fieldWithPath("data.devices[].info")
																			.type(JsonFieldType.STRING)
																			.description("장치 정보"),
																}))
												.build())));
	}
}
