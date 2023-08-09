package com.wifi.observer.client.wifi.dto.request.iptime;

import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.support.log.BulkRequestLogAble;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IptimeBulkHealthRequest extends WifiBulkHealthRequest implements BulkRequestLogAble {

	public IptimeBulkHealthRequest(List<CommonWifiHealthRequest> source) {
		super(source);
	}
}
