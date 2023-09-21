package com.wifi.observer.client.wifi.model;

import com.wifi.observer.client.wifi.model.info.HealthQueryInfo;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class HealthQueryClientModel {

	private final HealthQueryInfo statusInfo;
	private final String host;

	public static HealthQueryClientModel fail(String host) {
		return HealthQueryClientModel.builder()
				.statusInfo(HealthQueryInfo.builder().info(HttpStatus.BAD_REQUEST).build())
				.host(host)
				.build();
	}

	public boolean isFail() {
		return Objects.equals(statusInfo.getInfo(), HttpStatus.BAD_REQUEST);
	}

	public HttpStatus getHttpStatus() {
		return statusInfo.getInfo();
	}
}
