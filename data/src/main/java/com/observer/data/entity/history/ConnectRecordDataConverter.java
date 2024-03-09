package com.observer.data.entity.history;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class ConnectRecordDataConverter implements AttributeConverter<ConnectRecordData, String> {

	private final ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(ConnectRecordData attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ConnectRecordData convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, ConnectRecordData.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
