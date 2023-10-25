package com.wifi.obs.client.wifi.support.aop;

import static com.wifi.obs.client.wifi.support.aop.AspectLogFormat.REQUEST_LOG_FORMAT;

import com.wifi.obs.client.wifi.dto.function.ExistHost;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
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

	@Pointcut("@annotation(com.wifi.obs.client.wifi.support.log.WifiClientTrace)")
	public void clientTracePointCut() {}

	@Before("clientTracePointCut()")
	public void clientTraceLog(JoinPoint joinPoint) {
		Object source = getTraceSource(joinPoint);

		if (source instanceof ExistHost) {
			ExistHost hostLog = (ExistHost) source;
			String traceId = MDC.get("request_id");
			writeLog(joinPoint, traceId, hostLog);
		}
	}

	private Object getTraceSource(JoinPoint joinPoint) {
		int index = getRequestIndex(joinPoint);
		Object[] args = joinPoint.getArgs();
		return args[index];
	}

	private int getRequestIndex(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		return Integer.parseInt(method.getAnnotation(WifiClientTrace.class).index());
	}

	private String getMethodName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	private String getClassName(JoinPoint joinPoint) {
		return StringUtils.substringAfterLast(
				joinPoint.getSignature().getDeclaringType().getName(), ".");
	}

	private void writeLog(JoinPoint joinPoint, String traceId, ExistHost hostLog) {
		log.debug(
				REQUEST_LOG_FORMAT.getFormat(),
				traceId,
				getClassName(joinPoint) + "." + getMethodName(joinPoint),
				hostLog.getHost());
	}
}
