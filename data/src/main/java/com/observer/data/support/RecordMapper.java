package com.observer.data.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordMapper {

	private final ObjectMapper objectMapper;

	public String execute(RecordSupportInfo record) {
		try {
			return objectMapper.writeValueAsString(record);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Invalid record: " + record, e);
		}
	}
}
