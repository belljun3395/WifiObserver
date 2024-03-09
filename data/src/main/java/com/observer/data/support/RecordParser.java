package com.observer.data.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordParser {

	private final ObjectMapper objectMapper;

	public RecordSupportInfo execute(String record) {
		try {
			return objectMapper.readValue(record, RecordSupportInfo.class);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid record: " + record, e);
		}
	}
}
