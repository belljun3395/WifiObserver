package com.wifi.obs.app.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDescription {
	public static FieldDescriptor[] saveMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("멤버"),
			fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("멤버 id"),
			fieldWithPath("data.token").type(JsonFieldType.OBJECT).description("멤버 토큰"),
			fieldWithPath("data.token.accessToken").type(JsonFieldType.STRING).description("access 토큰"),
			fieldWithPath("data.token.refreshToken").type(JsonFieldType.STRING).description("refresh 토큰"),
		};
	}

	public static FieldDescriptor[] deleteMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}
}
