package com.wifi.obs.app.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class ServiceDescription {

	public static FieldDescriptor[] saveService() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}

	public static FieldDescriptor[] deleteService() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.NULL).description("null"),
		};
	}

	public static FieldDescriptor[] browseServiceInfos() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("서비스 정보"),
			fieldWithPath("data.wifiServiceInfos[]").type(JsonFieldType.ARRAY).description("서비스 리스트"),
			fieldWithPath("data.wifiServiceInfos[].id").type(JsonFieldType.NUMBER).description("서비스 id"),
			fieldWithPath("data.wifiServiceInfos[].type")
					.type(JsonFieldType.STRING)
					.description("서비스 타입"),
			fieldWithPath("data.wifiServiceInfos[].cycle")
					.type(JsonFieldType.NUMBER)
					.description("서비스 주기"),
			fieldWithPath("data.wifiServiceInfos[].status")
					.type(JsonFieldType.STRING)
					.description("서비스 상태"),
			fieldWithPath("data.wifiServiceInfos[].createAt")
					.type(JsonFieldType.STRING)
					.description("서비스 생성 일자"),
			fieldWithPath("data.wifiServiceInfos[].deviceInfos[]")
					.type(JsonFieldType.ARRAY)
					.description("서비스 등록 디바이스"),
			fieldWithPath("data.wifiServiceInfos[].deviceInfos[].id")
					.type(JsonFieldType.NUMBER)
					.description("서비스 등록 디바이스 id"),
			fieldWithPath("data.wifiServiceInfos[].deviceInfos[].type")
					.type(JsonFieldType.STRING)
					.description("서비스 등록 디바이스 타입"),
			fieldWithPath("data.wifiServiceInfos[].deviceInfos[].mac")
					.type(JsonFieldType.STRING)
					.description("서비스 등록 디바이스 mac"),
		};
	}

	public static FieldDescriptor[] browseUsers() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("서비스 유저 정보"),
			fieldWithPath("data.userInfos[]").type(JsonFieldType.ARRAY).description("서비스 유저 리스트"),
			fieldWithPath("data.userInfos[].mac").type(JsonFieldType.STRING).description("서비스 유저 mac"),
		};
	}

	public static FieldDescriptor[] health() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.STRING).description("서비스 상태"),
		};
	}

	public static FieldDescriptor[] serviceStet() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("서비스 통계"),
			fieldWithPath("data.serviceId").type(JsonFieldType.NUMBER).description("서비스 id"),
			fieldWithPath("data.stetInfos[]").type(JsonFieldType.ARRAY).description("서비스 통계 리스트"),
			fieldWithPath("data.stetInfos[].id").type(JsonFieldType.NUMBER).description("서비스 통계 장치 id"),
			fieldWithPath("data.stetInfos[].mac").type(JsonFieldType.STRING).description("서비스 통계 장치 mac"),
			fieldWithPath("data.stetInfos[].time")
					.type(JsonFieldType.NUMBER)
					.description("서비스 통계 장치 접속 시간"),
		};
	}
}
