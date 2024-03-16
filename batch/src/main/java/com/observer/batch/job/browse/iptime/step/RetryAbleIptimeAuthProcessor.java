package com.observer.batch.job.browse.iptime.step;

import com.observer.batch.job.browse.exception.RouterAuthException;
import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.data.config.JpaDataSourceConfig;
import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import com.observer.data.persistence.router.RouterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Import(IptimeAuthService.class)
public class RetryAbleIptimeAuthProcessor
		implements ItemProcessor<RouterEntity, RouterAuthResponse> {

	private final RetryTemplate retryTemplate;
	private final IptimeAuthProcessor processor;
	private final RouterRepository routerRepository;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public RouterAuthResponse process(RouterEntity item) {
		return retryTemplate.execute(
				new RetryCallback<RouterAuthResponse, RouterAuthException>() {
					@Override
					public RouterAuthResponse doWithRetry(RetryContext retryContext)
							throws RouterAuthException {
						return processor.process(item);
					}
				},
				new RecoveryCallback<RouterAuthResponse>() {
					@Override
					public RouterAuthResponse recover(RetryContext retryContext) {
						RouterEntity errorRouter = item.toBuilder().status(RouterStatus.ERROR).build();
						routerRepository.save(errorRouter);
						throw new RouterAuthException(item.toString());
					}
				});
	}
}
