package com.observer.domain.service.connection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RouterHostInfo {

	private Long routerId;
	private String serviceType;
	private String ip;
	private Long port;
	private String certification;
	private String password;
}
