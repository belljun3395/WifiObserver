package com.wifi.obs.app.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class DeviceDescription {

	public static FieldDescriptor[] saveDevice() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}

	public static FieldDescriptor[] editDevice() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}

	public static FieldDescriptor[] deleteDevice() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}

	public static FieldDescriptor[] browseServiceDevice() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("서비스 장치 조회"),
			fieldWithPath("data.devices").type(JsonFieldType.ARRAY).description("장치 목록"),
			fieldWithPath("data.devices[].id").type(JsonFieldType.NUMBER).description("장치 id"),
			fieldWithPath("data.devices[].type").type(JsonFieldType.STRING).description("장치 타입"),
			fieldWithPath("data.devices[].mac").type(JsonFieldType.STRING).description("장치 mac"),
		};
	}

	public static FieldDescriptor[] isConnectDevice() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("장치"),
			fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("장치 id"),
			fieldWithPath("data.mac").type(JsonFieldType.STRING).description("장치 mac"),
			fieldWithPath("data.connected").type(JsonFieldType.BOOLEAN).description("장치 접속여부"),
		};
	}

	public static FieldDescriptor[] deviceStet() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("장치 통계"),
			fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("장치 id"),
			fieldWithPath("data.mac").type(JsonFieldType.STRING).description("장치 mac"),
			fieldWithPath("data.time").type(JsonFieldType.NUMBER).description("장치 접속 시간"),
		};
	}
}
