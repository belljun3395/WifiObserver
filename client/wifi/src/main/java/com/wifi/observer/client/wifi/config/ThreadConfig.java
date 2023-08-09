package com.wifi.observer.client.wifi.config;

import static com.wifi.observer.client.wifi.WifiClientConfig.BEAN_NAME_PREFIX;

import com.wifi.observer.client.wifi.WifiClientConfig;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = {WifiClientConfig.BASE_PACKAGE})
public class ThreadConfig {

	private static final String REQUEST_ASYNC_EXECUTOR_BEAN_NAME =
			BEAN_NAME_PREFIX + "requestAsyncExecutor";
	private static final String HEALTH_ASYNC_EXECUTOR_BEAN_NAME =
			BEAN_NAME_PREFIX + "healthAsyncExecutor";
	private static String REQ_THREAD_NAME_PREFIX = "req-";
	private static String HEALTH_THREAD_NAME_PREFIX = "health-";

	@Value(value = "${thread.time.await}")
	private int AWAIT_TERMINATION_SECONDS;

	@Value(value = "${thread.req.core}")
	private int REQ_CORE_POOL_SIZE;

	@Value(value = "${thread.req.max}")
	private int REQ_MAX_POOL_SIZE;

	@Value(value = "${thread.req.queue}")
	private int REQ_QUEUE_CAPACITY;

	@Value(value = "${thread.health.core}")
	private int HEALTH_CORE_POOL_SIZE;

	@Value(value = "${thread.health.max}")
	private int HEALTH_MAX_POOL_SIZE;

	@Value(value = "${thread.health.queue}")
	private int HEALTH_QUEUE_CAPACITY;

	@Bean(REQUEST_ASYNC_EXECUTOR_BEAN_NAME)
	public Executor requestAsyncExecutor(LoggingTaskDecorator taskDecorator) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(REQ_CORE_POOL_SIZE);
		executor.setMaxPoolSize(REQ_MAX_POOL_SIZE);
		executor.setQueueCapacity(REQ_QUEUE_CAPACITY);
		executor.setThreadNamePrefix(REQ_THREAD_NAME_PREFIX);
		executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setTaskDecorator(taskDecorator);
		return executor;
	}

	@Bean(HEALTH_ASYNC_EXECUTOR_BEAN_NAME)
	public Executor healthAsyncExecutor(LoggingTaskDecorator taskDecorator) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(HEALTH_CORE_POOL_SIZE);
		executor.setMaxPoolSize(HEALTH_MAX_POOL_SIZE);
		executor.setThreadNamePrefix(HEALTH_THREAD_NAME_PREFIX);
		executor.setQueueCapacity(HEALTH_QUEUE_CAPACITY);
		executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setTaskDecorator(taskDecorator);
		return executor;
	}
}
