package com.observer.web.controller.connection;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.observer.ApiMain;
import com.observer.data.entity.device.DeviceType;
import com.observer.domain.dto.connection.ConnectionHistoryInfo;
import com.observer.domain.dto.connection.GetConnectionHistoryUseCaseResponse;
import com.observer.domain.dto.connection.IptimeCurrentConnectionUseCaseResponse;
import com.observer.domain.external.client.dto.connection.ConnectedUser;
import com.observer.domain.external.client.dto.connection.ConnectedUsers;
import com.observer.domain.usecase.connection.GetConnectionHistoryUseCase;
import com.observer.domain.usecase.connection.GetIptimeConnectionUsersUseCase;
import com.observer.web.controller.config.TestApiKeyUserDetailsService;
import com.observer.web.controller.description.Description;
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
class ConnectionControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean GetConnectionHistoryUseCase getConnectionHistoryUseCase;
	@MockBean GetIptimeConnectionUsersUseCase getIptimeConnectionUsersUseCase;

	private static final String TAG = "ConnectionController";
	private static final String BASE_URL = "/api/v1/connections";

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void history() throws Exception {
		List<ConnectionHistoryInfo> infos = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			infos.add(
					ConnectionHistoryInfo.builder()
							.deviceId((long) i)
							.routerId((long) i)
							.memberId((long) i)
							.type(DeviceType.NOTEBOOK.getType())
							.mac("00:00:00:00:00:00")
							.info("{ \"key\" : \"value\"}")
							.record("{ \"month\" : \"value\", \"day\" : \"value\"}")
							.build());
		}
		when(getConnectionHistoryUseCase.execute(any()))
				.thenReturn(GetConnectionHistoryUseCaseResponse.of(infos));

		mockMvc
				.perform(
						get(BASE_URL + "/history/{routerId}", 1L)
								.header("Wokey", "{{apiKey}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"GetConnectionHistory",
								resource(
										ResourceSnippetParameters.builder()
												.description("라우터에 속한 기기 목록의 해당 달 누적 접속 기록을 조회합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("GetConnectionHistoryRequest"))
												.requestHeaders(Description.apiKeyHeader())
												.responseSchema(Schema.schema("GetConnectionHistoryResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.infos")
																			.type(JsonFieldType.ARRAY)
																			.description("기기 목록의 해당 달 누적 접속 기록"),
																	fieldWithPath("data.infos[].deviceId")
																			.type(JsonFieldType.NUMBER)
																			.description("장치 id"),
																	fieldWithPath("data.infos[].routerId")
																			.type(JsonFieldType.NUMBER)
																			.description("라우터 id"),
																	fieldWithPath("data.infos[].memberId")
																			.type(JsonFieldType.NUMBER)
																			.description("멤버 id"),
																	fieldWithPath("data.infos[].type")
																			.type(JsonFieldType.STRING)
																			.description("장치 타입"),
																	fieldWithPath("data.infos[].mac")
																			.type(JsonFieldType.STRING)
																			.description("장치 mac 주소"),
																	fieldWithPath("data.infos[].info")
																			.type(JsonFieldType.STRING)
																			.description("장치 정보"),
																	fieldWithPath("data.infos[].record")
																			.type(JsonFieldType.STRING)
																			.description("기록")
																}))
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void iptimeConnect() throws Exception {
		List<ConnectedUser> users = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			users.add(ConnectedUser.builder().user("00:00:00:00:00:00").build());
		}
		when(getIptimeConnectionUsersUseCase.execute(any()))
				.thenReturn(
						IptimeCurrentConnectionUseCaseResponse.builder()
								.host("000.000.000.000:0000")
								.infos(ConnectedUsers.builder().users(users).build())
								.build());

		mockMvc
				.perform(
						get(BASE_URL + "/iptime/{routerId}", 1L)
								.header("Wokey", "{{apiKey}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"GetIptimeConnectionUsers",
								resource(
										ResourceSnippetParameters.builder()
												.description("Iptime 라우터에 접속한 사용자 목록을 조회합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("GetIptimeConnectionUsersRequest"))
												.requestHeaders(Description.apiKeyHeader())
												.responseSchema(Schema.schema("GetIptimeConnectionUsersResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.host")
																			.type(JsonFieldType.STRING)
																			.description("Iptime 라우터 접속 정보"),
																	fieldWithPath("data.infos")
																			.type(JsonFieldType.OBJECT)
																			.description("Iptime 라우터 접속자 정보"),
																	fieldWithPath("data.infos.users")
																			.type(JsonFieldType.ARRAY)
																			.description("사용자 목록"),
																	fieldWithPath("data.infos.users[].user")
																			.type(JsonFieldType.STRING)
																			.description("사용자 mac 주소"),
																}))
												.build())));
	}
}
