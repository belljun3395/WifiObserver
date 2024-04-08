package com.observer.batch.job.browse.iptime.step;

import com.observer.batch.job.browse.exception.AbstractRouterException;
import com.observer.batch.job.browse.exception.RouterBrowseException;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import com.observer.data.persistence.router.RouterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryAbleIptimeBrowseProcessor
		implements ItemProcessor<RouterAuthResponse, RouterUsersResponse> {

	private final RetryTemplate retryTemplate;
	private final IptimeBrowseProcessor processor;
	private final RouterRepository routerRepository;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public RouterUsersResponse process(RouterAuthResponse item) {
		return retryTemplate.execute(
				new RetryCallback<RouterUsersResponse, AbstractRouterException>() {
					@Override
					public RouterUsersResponse doWithRetry(RetryContext retryContext)
							throws AbstractRouterException {
						return processor.process(item);
					}
				},
				new RecoveryCallback<RouterUsersResponse>() {
					@Override
					public RouterUsersResponse recover(RetryContext retryContext) {
						RouterEntity routerEntity =
								routerRepository
										.findByHostAndDeletedFalse(item.getHost())
										.orElseThrow(() -> new RouterBrowseException(item.toString()));
						RouterEntity errorRouter = routerEntity.toBuilder().status(RouterStatus.ERROR).build();
						routerRepository.save(errorRouter);
						return null;
					}
				});
	}
}
