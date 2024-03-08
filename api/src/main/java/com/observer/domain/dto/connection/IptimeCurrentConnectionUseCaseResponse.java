package com.observer.domain.dto.connection;

import com.observer.domain.external.client.dto.connection.ConnectedUsers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class IptimeCurrentConnectionUseCaseResponse {

	private final String host;
	private final ConnectedUsers infos;
}
