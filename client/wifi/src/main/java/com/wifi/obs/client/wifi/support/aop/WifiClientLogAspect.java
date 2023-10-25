package com.wifi.obs.client.wifi.support.aop;

import static com.wifi.obs.client.wifi.support.aop.AspectLogFormat.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifi.obs.client.wifi.dto.function.ExistHost;
import com.wifi.obs.client.wifi.support.log.LogAbleBulkRequest;
import com.wifi.obs.client.wifi.support.log.WifiLogData;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WifiClientLogAspect {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String ASYNC_TAG = "async";
	private static final String SYNC_TAG = "sync";

	private final WifiExceptionHandlerMapper wifiExceptionHandlerMapper;
	private final ObjectMapper objectMapper;

	@Pointcut(value = "execution(* com.wifi.obs.client.wifi.client.*.*(..))")
	public void wifiClientPointCut() {}

	@Around("wifiClientPointCut()")
	public Object clientLog(ProceedingJoinPoint pjp) throws Throwable {
		long currentTimeMillis = System.currentTimeMillis();
		String timeStamp = getTimeStamp(currentTimeMillis);

		String traceId = MDC.get("request_id");

		String className = getClassName(pjp);
		String methodName = getMethodName(pjp);

		String isAsync = isAsync(methodName);

		writePreLog(traceId, methodName, pjp);

		Object proceed = pjp.proceed();

		WifiLogData data =
				getLogData(traceId, timeStamp, currentTimeMillis, className, methodName, isAsync);

		String jsonData = objectMapper.writeValueAsString(data);
		MDC.put("client.wifi", jsonData);

		writePostLog(jsonData);
		return proceed;
	}

	@AfterThrowing(pointcut = "wifiClientPointCut()", throwing = "ex")
	public void clientExceptionLog(Throwable ex) {
		wifiExceptionHandlerMapper.handleClientConnectionException(ex);
	}

	private String getTimeStamp(Long currentTimeMillis) {
		Date date = new Date(currentTimeMillis);
		SimpleDateFormat newDtFormat = new SimpleDateFormat(DATE_FORMAT);
		return newDtFormat.format(date);
	}

	private String getClassName(JoinPoint joinPoint) {
		return StringUtils.substringAfterLast(
				joinPoint.getSignature().getDeclaringType().getName(), ".");
	}

	private String getMethodName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	private String isAsync(String methodName) {
		String tag = "";
		if (methodName.contains("Async")) {
			tag = ASYNC_TAG;
		} else {
			tag = SYNC_TAG;
		}
		return tag;
	}

	private void writePreLog(String traceId, String methodName, ProceedingJoinPoint pjp) {
		if (methodName.contains("queries")) {
			log.debug(START_BULK_REQUEST_LOG_FORMAT.getFormat(), traceId);
			writeRequestsLog(pjp);
			return;
		}
		log.debug(START_REQUEST_LOG_FORMAT.getFormat(), traceId);
	}

	private void writePostLog(String jsonData) {
		log.debug(END_REQUEST_LOG_FORMAT.getFormat(), jsonData);
	}

	private void writeRequestsLog(ProceedingJoinPoint pjp) {
		int idx = getRequestsParameterIndex(pjp);
		Object arg = pjp.getArgs()[idx];
		LogAbleBulkRequest requests = (LogAbleBulkRequest) arg;
		List<? extends ExistHost> source = requests.getSource();
		log.debug(
				REQUESTS_LOG_FORMAT.getFormat(),
				source.size(),
				source.stream().map(ExistHost::getHost).collect(Collectors.toList()));
	}

	private int getRequestsParameterIndex(ProceedingJoinPoint pjp) {
		int idx = 0;
		Annotation[][] parameterAnnotations =
				((MethodSignature) pjp.getSignature()).getMethod().getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			boolean flag = false;
			for (int j = 0; j < parameterAnnotations[i].length; j++) {
				if (LogAbleBulkRequest.class.equals(parameterAnnotations[i][j].annotationType())) {
					flag = true;
					break;
				}
			}
			if (flag) {
				idx = i;
				break;
			}
		}
		return idx;
	}

	private WifiLogData getLogData(
			String traceId,
			String timeStamp,
			long currentTimeMillis,
			String className,
			String methodName,
			String isAsync) {
		return WifiLogData.builder()
				.traceId(traceId)
				.timestamp(timeStamp)
				.duration((System.currentTimeMillis() - currentTimeMillis) + "ms")
				.actor(className + "." + methodName)
				.tag(isAsync)
				.build();
	}
}
