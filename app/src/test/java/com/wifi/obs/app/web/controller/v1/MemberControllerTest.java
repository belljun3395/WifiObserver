package com.wifi.obs.app.web.controller.v1;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifi.obs.app.AppMain;
import com.wifi.obs.app.domain.dto.response.member.SavedMemberInfo;
import com.wifi.obs.app.domain.usecase.member.DeleteMemberUseCase;
import com.wifi.obs.app.domain.usecase.member.SaveMemberUseCase;
import com.wifi.obs.app.support.token.AuthToken;
import com.wifi.obs.app.web.controller.v1.description.Description;
import com.wifi.obs.app.web.controller.v1.description.MemberDescription;
import com.wifi.obs.app.web.dto.request.member.SaveMemberRequest;
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
class MemberControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private SaveMemberUseCase saveMemberUseCase;
	@MockBean private DeleteMemberUseCase deleteMemberUseCase;

	private static final String TAG = "Member";
	private static final String BASE_URL = "/api/v1/members";

	@Test
	void saveMember() throws Exception {
		SaveMemberRequest request =
				SaveMemberRequest.builder().email("email@email.com").password("password").build();

		SavedMemberInfo res =
				SavedMemberInfo.builder()
						.id(1L)
						.token(
								AuthToken.builder().accessToken("accessToken").refreshToken("refreshToken").build())
						.build();

		when(saveMemberUseCase.execute(any(SaveMemberRequest.class))).thenReturn(res);

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(post(BASE_URL, 0).content(content).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("회원 가입, 로그인")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMemberRequest"))
												.responseSchema(Schema.schema("SaveMemberResponse"))
												.responseFields(Description.success(MemberDescription.saveMember()))
												.build())));
	}

	@Test
	void deleteMember() throws Exception {

		mockMvc
				.perform(
						delete(BASE_URL, 0)
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
												.requestHeaders(Description.authHeader())
												.responseSchema(Schema.schema("DeleteMemberResponse"))
												.responseFields(Description.success(MemberDescription.deleteMember()))
												.build())));
	}
}
