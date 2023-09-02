package com.wifi.obs.app.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class Description {

	private static FieldDescriptor getSuccessCodeDescriptor() {
		return fieldWithPath("code").type(JsonFieldType.STRING).description("success");
	}

	private static FieldDescriptor getSuccessMessageDescriptor() {
		return fieldWithPath("message").type(JsonFieldType.STRING).description("성공");
	}

	public static FieldDescriptor[] success(FieldDescriptor[] data) {
		return ArrayUtils.addAll(data, getSuccessMessageDescriptor(), getSuccessCodeDescriptor());
	}
}
