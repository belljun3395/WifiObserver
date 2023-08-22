package com.wifi.observer.client.wifi.support.aop;

import static com.wifi.observer.client.wifi.support.log.WifiLogProperties.WIFI_TRACEID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifi.observer.client.wifi.support.log.BulkRequestLogAble;
import com.wifi.observer.client.wifi.support.log.HostLogAble;
import com.wifi.observer.client.wifi.support.log.WifiExceptionHandlerMapper;
import com.wifi.observer.client.wifi.support.log.WifiLogData;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

	private final WifiExceptionHandlerMapper wifiExceptionHandlerMapper;
	private final ObjectMapper objectMapper;

	@Pointcut(value = "execution(* com.wifi.observer.client.wifi.client.*.*(..))")
	public void wifiClientPointCut() {}

	@Around("wifiClientPointCut()")
	public Object clientLog(ProceedingJoinPoint pjp) throws Throwable {
		long currentTimeMillis = System.currentTimeMillis();
		String timeStamp = getTimeStamp(currentTimeMillis);

		String traceId = UUID.randomUUID().toString();
		MDC.put(WIFI_TRACEID.getKey(), traceId);

		String className = getClassName(pjp);
		String methodName = getMethodName(pjp);

		String isAsync = isAsync(methodName);

		printStartLog(traceId, methodName, pjp);

		Object proceed = pjp.proceed();

		WifiLogData data =
				WifiLogData.builder()
						.traceId(traceId)
						.timestamp(timeStamp)
						.duration((System.currentTimeMillis() - currentTimeMillis) + "ms")
						.actor(className + "." + methodName)
						.tag(isAsync)
						.build();

		String jsonData = objectMapper.writeValueAsString(data);
		MDC.put("client.wifi", jsonData);

		log.info("End Request! client log : {}", jsonData);
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

	private String getMethodName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	private String getClassName(JoinPoint joinPoint) {
		return StringUtils.substringAfterLast(
				joinPoint.getSignature().getDeclaringType().getName(), ".");
	}

	private void printStartLog(String traceId, String methodName, ProceedingJoinPoint pjp) {
		if (methodName.contains("queries")) {
			log.info("Start Bulk Request! TraceId is {}", traceId);
			printEachRequestLog(pjp);
			return;
		}
		log.info("Start Request! TraceId is {}", traceId);
	}

	private void printEachRequestLog(ProceedingJoinPoint pjp) {
		int idx = getBulkRequestParameterIndex(pjp);
		Object arg = pjp.getArgs()[idx];
		BulkRequestLogAble requests = (BulkRequestLogAble) arg;
		List<? extends HostLogAble> source = requests.getSource();
		log.info(
				"request count : {} {}",
				source.size(),
				source.stream().map(HostLogAble::getHost).collect(Collectors.toList()));
	}

	private int getBulkRequestParameterIndex(ProceedingJoinPoint pjp) {
		int idx = 0;
		Annotation[][] parameterAnnotations =
				((MethodSignature) pjp.getSignature()).getMethod().getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			boolean flag = false;
			for (int j = 0; j < parameterAnnotations[i].length; j++) {
				if (BulkRequestLogAble.class.equals(parameterAnnotations[i][j].annotationType())) {
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

	private String isAsync(String methodName) {
		String tag;
		if (methodName.contains("Async")) {
			tag = "async";
		} else {
			tag = "sync";
		}
		return tag;
	}
}
