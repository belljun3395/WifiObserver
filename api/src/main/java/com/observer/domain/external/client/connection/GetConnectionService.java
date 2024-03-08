package com.observer.domain.external.client.connection;

import com.observer.domain.external.client.dto.connection.ConnectedUsersRequest;
import com.observer.domain.external.client.dto.connection.ConnectedUsersResponse;

public interface GetConnectionService {

	ConnectedUsersResponse execute(ConnectedUsersRequest request);
}
