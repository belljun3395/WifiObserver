package com.observer.batch.job.browse.iptime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class IptimeRouterRetryConfig {

	private static final long BACK_OFF_PERIOD = 1000L;
	private static final int MAX_ATTEMPTS = 3;

	@Bean(name = "iptimeBrowseRetryTemplate")
	public RetryTemplate iptimeBrowseRetryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(BACK_OFF_PERIOD);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}
}
