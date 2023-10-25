package com.wifi.obs.client.wifi.model.value;

import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class HealthQueryVO implements HttpStatusVO {

	private final HttpStatusResponse info;
}
