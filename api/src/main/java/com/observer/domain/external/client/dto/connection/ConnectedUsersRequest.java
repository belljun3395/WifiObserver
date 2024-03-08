package com.observer.domain.external.client.dto.connection;

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
public class ConnectedUsersRequest {
	private String ip;
	private Long port;
	private String certification;
	private String password;
}
