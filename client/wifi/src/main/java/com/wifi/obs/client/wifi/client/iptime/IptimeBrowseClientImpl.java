package com.wifi.obs.client.wifi.client.iptime;

import static com.wifi.obs.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.obs.client.wifi.exception.WifiRuntimeException;
import com.wifi.obs.client.wifi.http.HTMLResponse;
import com.wifi.obs.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.obs.client.wifi.model.Users;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.obs.client.wifi.util.resolver.users.IptimeDocumentUsersOnConnectFilterDecorator;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseClientImpl extends IptimeBrowseClient {

	private final IptimeBrowseConverter iptimeBrowseConverter;
	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeDocumentUsersOnConnectFilterDecorator
			iptimeDocumentUsersOnConnectFilterDecorator;
	private final BrowseClientQuery browseClientQuery;

	private final ErrorNotificationService errorNotificationService;

	@Override
	protected WifiBrowseRequestElement getRequestElement(IptimeBrowseRequest request) {
		Map<String, String> headers = iptimeBrowseClientHeaderGenerator.execute(request.getHost());

		return iptimeBrowseConverter.to(request, headers, request.getAuthInfo());
	}

	@Override
	protected List<String> executeQuery(WifiBrowseRequestElement data) {
		HTMLResponse response = browseClientQuery.query(data);
		if (response.isFail()) {
			return new ArrayList<>();
		}
		return iptimeDocumentUsersOnConnectFilterDecorator.resolve(response);
	}

	@Override
	protected ClientResponse<OnConnectUserInfos> getClientResponse(Users source) {
		return iptimeBrowseConverter.from(source.getUsers(), source.getHost());
	}

	@Override
	protected void writeFailLog(String host) {
		String className = this.getClass().getName();
		log.warn(RESPONSE_ERROR.getFormat(), className, host);
		errorNotificationService.sendNotification(
				format(RESPONSE_ERROR.getSlackFormat(), className, host));
	}

	@Override
	protected ClientResponse<OnConnectUserInfos> requestQuery(IptimeBrowseRequest request) {
		try {
			return this.query(request);
		} catch (WifiRuntimeException e) {
			return IptimeOnConnectUserInfosResponse.fail(request.getHost());
		}
	}
}
