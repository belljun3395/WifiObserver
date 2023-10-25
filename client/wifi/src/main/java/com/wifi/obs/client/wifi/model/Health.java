package com.wifi.obs.client.wifi.model;

import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.model.value.HealthQueryVO;
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
public class Health {

	private final HealthQueryVO statusInfo;
	private final String host;

	public static Health fail(String host) {
		return Health.builder()
				.statusInfo(
						HealthQueryVO.builder().info(HttpStatusResponse.of(HttpStatus.BAD_REQUEST)).build())
				.host(host)
				.build();
	}

	public boolean isFail() {
		return Objects.equals(statusInfo.getInfo(), HttpStatusResponse.of(HttpStatus.BAD_REQUEST));
	}

	public HttpStatusResponse getHttpStatus() {
		return statusInfo.getInfo();
	}
}
