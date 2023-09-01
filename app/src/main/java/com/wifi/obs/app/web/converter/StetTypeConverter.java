package com.wifi.obs.app.web.converter;

import com.wifi.obs.app.web.dto.request.StetType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StetTypeConverter implements Converter<String, StetType> {

	@Override
	public StetType convert(String source) {
		return StetType.codeOf(source.toLowerCase());
	}
}
