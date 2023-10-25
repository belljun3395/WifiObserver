package com.wifi.obs.app.domain.service.wifi.iptime;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.service.wifi.GetUserService;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.web.dto.request.beta.IptimeBetaRequest;
import com.wifi.obs.client.wifi.client.iptime.IptimeAuthClient;
import com.wifi.obs.client.wifi.client.iptime.IptimeBrowseClient;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfo;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetIptimeUserService implements GetUserService {

	private final IptimeAuthClient iptimeAuthClient;
	private final IptimeBrowseClient iptimeBrowseClient;

	@Override
	public OnConnectUserInfos execute(WifiAuthEntity authInfo) {
		return this.execute(getRequest(authInfo));
	}

	public OnConnectUserInfos execute(IptimeBetaRequest request) {
		return this.execute(getRequest(request));
	}

	private OnConnectUserInfos execute(IptimeAuthRequest authRequest) {
		ClientResponse<AuthInfo> authResponse = iptimeAuthClient.command(authRequest);

		AuthInfo auth = authResponse.getResponse().orElseThrow(ClientProblemException::new);
		String host = authResponse.getHost();

		com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos res =
				iptimeBrowseClient
						.query(IptimeBrowseRequest.builder().authInfo(auth.getInfo()).host(host).build())
						.getResponse()
						.orElseThrow(ClientProblemException::new);

		List<OnConnectUserInfo> usersInfo = res.getUsers();

		List<UserInfo> userInfos =
				usersInfo.stream()
						.map(userInfo -> UserInfo.builder().mac(userInfo.getUser()).build())
						.collect(Collectors.toList());

		return OnConnectUserInfos.of(userInfos);
	}

	private IptimeAuthRequest getRequest(WifiAuthEntity authInfo) {
		return IptimeAuthRequest.builder()
				.host(authInfo.getHost())
				.userName(authInfo.getCertification())
				.password(authInfo.getPassword())
				.build();
	}

	private IptimeAuthRequest getRequest(IptimeBetaRequest request) {
		return IptimeAuthRequest.builder()
				.host(request.getHost() + ":" + request.getPort())
				.userName(request.getCertification())
				.password(request.getPassword())
				.build();
	}
}
