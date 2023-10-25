package com.wifi.obs.client.wifi.dto.request.iptime;

import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IptimeBulkHealthRequest extends WifiBulkHealthRequest<WifiHostRequest> {

	public IptimeBulkHealthRequest(List<WifiHostRequest> source) {
		super(source);
	}
}
