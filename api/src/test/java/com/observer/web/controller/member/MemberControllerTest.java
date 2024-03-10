package com.observer.web.controller.member;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.observer.ApiMain;
import com.observer.domain.dto.member.CheckCertificationDuplicationUseCaseResponse;
import com.observer.domain.dto.member.LoginMemberUseCaseResponse;
import com.observer.domain.dto.member.RenewalApiKeyUseCaseResponse;
import com.observer.domain.usecase.member.CheckCertificationDuplicationUseCase;
import com.observer.domain.usecase.member.DeleteMemberUseCase;
import com.observer.domain.usecase.member.LoginMemberUseCase;
import com.observer.domain.usecase.member.PostMemberUseCase;
import com.observer.domain.usecase.member.RenewalApiKeyUseCase;
import com.observer.web.controller.config.TestApiKeyUserDetailsService;
import com.observer.web.controller.description.Description;
import com.observer.web.controller.dto.member.LoginRequest;
import com.observer.web.controller.dto.member.MemberDeleteRequest;
import com.observer.web.controller.dto.member.MemberSaveRequest;
import com.observer.web.controller.dto.member.RenewalApiKeyRequest;
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
class MemberControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean PostMemberUseCase postMemberUseCase;
	@MockBean LoginMemberUseCase loginMemberUseCase;
	@MockBean DeleteMemberUseCase deleteMemberUseCase;
	@MockBean RenewalApiKeyUseCase renewalApiKeyUseCase;
	@MockBean CheckCertificationDuplicationUseCase checkCertificationDuplicationUseCase;

	private static final String TAG = "MemberController";
	private static final String BASE_URL = "/api/v1/members";

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void save() throws Exception {
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification("test123").password("password@123").build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(post(BASE_URL, 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("회원 가입")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMemberRequest"))
												.responseSchema(Schema.schema("SaveMemberResponse"))
												.responseFields(Description.created())
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void login() throws Exception {
		LoginRequest request =
				LoginRequest.builder().certification("test123").password("password@123").build();

		String content = objectMapper.writeValueAsString(request);

		LoginMemberUseCaseResponse response =
				LoginMemberUseCaseResponse.builder().apiKey("31M0RSXT6Y7R29P7WN8ZA7H6706ID14F").build();

		when(loginMemberUseCase.execute(any())).thenReturn(response);

		mockMvc
				.perform(
						post(BASE_URL + "/login", 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"LoginMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("로그인")
												.tag(TAG)
												.requestSchema(Schema.schema("LoginMemberRequest"))
												.responseSchema(Schema.schema("LoginMemberResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.apiKey")
																			.type(JsonFieldType.STRING)
																			.description("로그인 성공 시 발급된 API Key")
																}))
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void delete() throws Exception {

		MemberDeleteRequest request = MemberDeleteRequest.builder().password("password@123").build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL + "/delete", 0)
								.content(content)
								.header("Authorization", "{{accessToken}}")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeleteMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("회원 탈퇴")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteMemberRequest"))
												.requestHeaders(Description.apiKeyHeader())
												.responseSchema(Schema.schema("DeleteMemberResponse"))
												.responseFields(Description.deleted())
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void renewalApiKey() throws Exception {

		RenewalApiKeyRequest request =
				RenewalApiKeyRequest.builder().certification("test123").password("password@123").build();

		RenewalApiKeyUseCaseResponse response =
				RenewalApiKeyUseCaseResponse.builder().apiKey("31M0RSXT6Y7R29P7WN8ZA7H6706ID14F").build();

		String content = objectMapper.writeValueAsString(request);

		when(renewalApiKeyUseCase.execute(any())).thenReturn(response);

		mockMvc
				.perform(put(BASE_URL + "/key", 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"RenewalApiKey",
								resource(
										ResourceSnippetParameters.builder()
												.description("ApiKey 갱신")
												.tag(TAG)
												.requestSchema(Schema.schema("RenewalApiKeyRequest"))
												.responseSchema(Schema.schema("RenewalApiKeyResponse"))
												.responseFields(
														Description.modified(
																new FieldDescriptor[] {
																	fieldWithPath("data.apiKey")
																			.type(JsonFieldType.STRING)
																			.description("갱신된 API Key")
																}))
												.build())));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testApiKeyUserDetailsService")
	void check() throws Exception {

		CheckCertificationDuplicationUseCaseResponse response =
				CheckCertificationDuplicationUseCaseResponse.builder().duplication(false).build();

		when(checkCertificationDuplicationUseCase.execute(any())).thenReturn(response);

		mockMvc
				.perform(
						get(BASE_URL + "/check/certification/{certification}", "certification")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"CheckCertificationDuplication",
								resource(
										ResourceSnippetParameters.builder()
												.description("Certification 중복 확인")
												.tag(TAG)
												.requestSchema(Schema.schema("CheckCertificationDuplicationRequest"))
												.responseSchema(Schema.schema("CheckCertificationDuplicationResponse"))
												.responseFields(
														Description.success(
																new FieldDescriptor[] {
																	fieldWithPath("data.duplication")
																			.type(JsonFieldType.BOOLEAN)
																			.description("Certification 중복 여부")
																}))
												.build())));
	}
}
