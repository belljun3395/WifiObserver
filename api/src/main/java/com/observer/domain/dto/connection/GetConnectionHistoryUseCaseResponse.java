package com.observer.domain.dto.connection;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetConnectionHistoryUseCaseResponse {

	private List<ConnectionHistoryInfo> infos;

	public static GetConnectionHistoryUseCaseResponse of(List<ConnectionHistoryInfo> infos) {
		return new GetConnectionHistoryUseCaseResponse(infos);
	}
}
