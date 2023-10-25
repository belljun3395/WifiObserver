package com.wifi.obs.client.wifi.support.aop;

import com.wifi.obs.client.wifi.exception.WifiCompletionException;
import com.wifi.obs.client.wifi.exception.WifiURISyntaxException;
import com.wifi.obs.client.wifi.support.aop.annotation.WifiClientAdvice;
import com.wifi.obs.client.wifi.support.aop.annotation.WifiClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@WifiClientAdvice
@RequiredArgsConstructor
public class WifiClientExceptionAdvice {

	private static final String LOG_MESSAGE_FORMAT = "error at traceId {}\n {}";
	private static final String UNCAUGHT_LOG_MESSAGE = "??";

	@WifiClientExceptionHandler({WifiURISyntaxException.class})
	public final void handleClientConnectionException(final Exception ex) {
		writeLog(ex, MDC.get("request_id"));
	}

	@WifiClientExceptionHandler({WifiCompletionException.class})
	public final void handleCompletionException(final Exception ex) {
		writeLog(ex, MDC.get("request_id"));
	}

	@WifiClientExceptionHandler({Exception.class})
	public final void handleClientUtilException(final Exception ex) {
		writeLog(ex, MDC.get("request_id"));
	}

	private void writeLog(final Exception ex, String traceId) {
		try {
			log.warn(LOG_MESSAGE_FORMAT, traceId, ExceptionUtils.getStackTrace(ex));
		} catch (Exception e) {
			log.warn(LOG_MESSAGE_FORMAT, UNCAUGHT_LOG_MESSAGE, ExceptionUtils.getStackTrace(e));
		}
	}
}
