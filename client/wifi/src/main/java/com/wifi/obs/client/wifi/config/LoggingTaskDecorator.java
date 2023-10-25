package com.wifi.obs.client.wifi.config;

import static com.wifi.obs.client.wifi.WifiClientConfig.BEAN_NAME_PREFIX;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

@Component(value = BEAN_NAME_PREFIX + "LoggingTaskDecorator")
public class LoggingTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable task) {

		Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
		return () -> {
			if (callerThreadContext != null) {
				MDC.setContextMap(callerThreadContext);
			}
			task.run();
		};
	}
}
