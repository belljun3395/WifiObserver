package com.wifi.observer.client.wifi.support.aop;

import static com.wifi.observer.client.wifi.support.log.WifiLogProperties.WIFI_TRACEID;

import com.wifi.observer.client.wifi.support.log.HostLogAble;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import com.wifi.observer.client.wifi.support.log.WifiLogProperties;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class WifiClientTraceLogAspect {

	@Pointcut("@annotation(com.wifi.observer.client.wifi.support.log.WifiClientTrace)")
	public void clientTracePointCut() {}

	@Before("clientTracePointCut()")
	public void setMDCLog(JoinPoint joinPoint) {
		Object source = getSourceRequest(joinPoint);

		if (source instanceof HostLogAble) {
			setWifiHealthRequestLog((HostLogAble) source);
		}
	}

	@After(value = "clientTracePointCut()")
	public void getMDCLog(JoinPoint joinPoint) {
		String traceId = MDC.get(WIFI_TRACEID.getKey());
		log.info(
				"{} {} to {}",
				traceId,
				getClassName(joinPoint) + "." + getMethodName(joinPoint),
				MDC.get(WifiLogProperties.HOST.getKey()));
	}

	private Object getSourceRequest(JoinPoint joinPoint) {
		int index = getRequestIndex(joinPoint);
		Object[] args = joinPoint.getArgs();
		return args[index];
	}

	private int getRequestIndex(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		return Integer.parseInt(method.getAnnotation(WifiClientTrace.class).index());
	}

	private void setWifiHealthRequestLog(HostLogAble source) {
		MDC.put(WifiLogProperties.HOST.getKey(), source.getHost());
	}

	private String getMethodName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	private String getClassName(JoinPoint joinPoint) {
		return StringUtils.substringAfterLast(
				joinPoint.getSignature().getDeclaringType().getName(), ".");
	}
}
