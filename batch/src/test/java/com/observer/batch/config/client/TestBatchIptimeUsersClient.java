package com.observer.batch.config.client;

import com.observer.batch.job.browse.iptime.client.BatchIptimeUsersClient;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterUser;
import com.observer.client.router.support.dto.response.RouterUsers;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestBatchIptimeUsersClient implements BatchIptimeUsersClient {

	static List<String> EXPECT_EXTRACT_ON_CONNECT_USERS =
			List.of(
					"D6:98:68:C3:6B:36",
					"D6:38:41:90:BC:63",
					"3C:9C:0F:60:C8:8B",
					"90:32:4B:18:00:1B",
					"BC:D0:74:9F:B0:E3");

	@Override
	public RouterUsersResponse execute(IptimeUsersServiceRequest request) throws ClientException {
		List<RouterUser> users =
				EXPECT_EXTRACT_ON_CONNECT_USERS.stream().map(RouterUser::new).collect(Collectors.toList());
		return RouterUsersResponse.builder()
				.response(RouterUsers.builder().users(users).build())
				.build();
	}
}
