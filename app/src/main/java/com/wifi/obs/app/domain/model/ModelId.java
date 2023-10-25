package com.wifi.obs.app.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class ModelId {

	private final Long id;

	public static ModelId of(Long id) {
		return new ModelId(id);
	}

	public boolean isSame(Long target) {
		return id.equals(target);
	}
}
