package com.wifi.observer.client.wifi.dto.request.iptime;

import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.support.log.BulkRequestLogAble;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IptimeBulkBrowseRequest extends WifiBulkBrowseRequest<IptimeBrowseRequest>
		implements BulkRequestLogAble {
	IptimeBulkBrowseRequest(List<IptimeBrowseRequest> source) {
		super(source);
	}

	public static IptimeBulkBrowseRequest of(List<IptimeBrowseRequest> source) {
		return new IptimeBulkBrowseRequest(source);
	}
}
