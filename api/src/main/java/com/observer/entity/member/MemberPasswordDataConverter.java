package com.observer.entity.member;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MemberPasswordDataConverter implements AttributeConverter<MemberPasswordData, String> {

	@Override
	public String convertToDatabaseColumn(MemberPasswordData attribute) {
		return attribute.getPassword();
	}

	@Override
	public MemberPasswordData convertToEntityAttribute(String dbData) {
		return MemberPasswordData.builder().password(dbData).build();
	}
}
