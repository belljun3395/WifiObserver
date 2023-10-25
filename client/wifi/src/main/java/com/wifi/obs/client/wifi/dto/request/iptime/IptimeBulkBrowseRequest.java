package com.wifi.obs.client.wifi.dto.request.iptime;

import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class IptimeBulkBrowseRequest extends WifiBulkBrowseRequest<IptimeBrowseRequest> {

	IptimeBulkBrowseRequest(List<IptimeBrowseRequest> source) {
		super(source);
	}

	public static IptimeBulkBrowseRequest of(List<IptimeBrowseRequest> source) {
		return new IptimeBulkBrowseRequest(source);
	}
}
