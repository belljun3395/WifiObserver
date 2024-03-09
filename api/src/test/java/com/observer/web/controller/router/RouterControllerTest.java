package com.observer.web.controller.router;

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
import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterServiceType;
import com.observer.domain.dto.router.GetRoutersUseCaseResponse;
import com.observer.domain.dto.router.PostRouterUseCaseResponse;
import com.observer.domain.usecase.router.DeleteRouterUseCase;
import com.observer.domain.usecase.router.GetRoutersUseCase;
import com.observer.domain.usecase.router.PostRouterUseCase;
import com.observer.web.controller.config.TestApiKeyUserDetailsService;
import com.observer.web.controller.description.Description;
import com.observer.web.controller.dto.router.DeleteRouterRequest;
import com.observer.web.controller.dto.router.PostRouterRequest;
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
class RouterControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean PostRouterUseCase postRouterUseCase;
	@MockBean DeleteRouterUseCase deleteRouterUseCase;
	@MockBean GetRoutersUseCase getRoutersUseCase;

	private static final String TAG = "RouterController";
	private static final String BASE_URL = "/api/v1/routers";

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void save() throws Exception {
		PostRouterRequest request =
				PostRouterRequest.builder()
						.host("000.000.000.000:0000")
						.certification("certification")
						.password("password")
						.build();

		String content = objectMapper.writeValueAsString(request);

		when(postRouterUseCase.execute(any()))
				.thenReturn(
						PostRouterUseCaseResponse.builder()
								.routerId(1L)
								.serviceType("IPTIME")
								.host("000.000.000.000:0000")
								.build());

		mockMvc
				.perform(post(BASE_URL, 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"PostRouter",
								resource(
										ResourceSnippetParameters.builder()
												.description("새로운 라우터를 등록합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("PostRouterRequest"))
												.responseSchema(Schema.schema("PostRouterResponse"))
												.responseFields(
														Description.created(
																new FieldDescriptor[] {
																	fieldWithPath("data.routerId")
																			.type(JsonFieldType.NUMBER)
																			.description("라우터 식별자"),
																	fieldWithPath("data.serviceType")
																			.type(JsonFieldType.STRING)
																			.description("라우터 서비스 타입"),
																	fieldWithPath("data.host")
																			.type(JsonFieldType.STRING)
																			.description("라우터 호스트"),
																}))
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void delete() throws Exception {
		DeleteRouterRequest request = DeleteRouterRequest.builder().routerId(1L).build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL + "/delete", 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeleteRouter",
								resource(
										ResourceSnippetParameters.builder()
												.description("라우터를 삭제합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteRouterRequest"))
												.responseSchema(Schema.schema("DeleteRouterResponse"))
												.responseFields(Description.deleted())
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void browse() throws Exception {
		List<RouterEntity> routers = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			routers.add(
					RouterEntity.builder()
							.id((long) i)
							.serviceType(RouterServiceType.IPTIME)
							.host(i + "00.000.000.000:0000")
							.build());
		}
		when(getRoutersUseCase.execute(any())).thenReturn(GetRoutersUseCaseResponse.from(routers));

		mockMvc
				.perform(get(BASE_URL, 0).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"BrowseRouters",
								resource(
										ResourceSnippetParameters.builder()
												.description("라우터 목록을 조회합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("BrowseRoutersRequest"))
												.responseSchema(Schema.schema("BrowseRoutersResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.routers[]")
																			.type(JsonFieldType.ARRAY)
																			.description("라우터 목록"),
																	fieldWithPath("data.routers[].routerId")
																			.type(JsonFieldType.NUMBER)
																			.description("라우터 식별자"),
																	fieldWithPath("data.routers[].serviceType")
																			.type(JsonFieldType.STRING)
																			.description("라우터 서비스 타입"),
																	fieldWithPath("data.routers[].host")
																			.type(JsonFieldType.STRING)
																			.description("라우터 호스트"),
																}))
												.build())));
	}
}
