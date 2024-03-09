package com.observer.config.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExternalLogAspect {

	/** DAO 패키지 내의 모든 메소드에 대해 로깅을 수행한다. */
	@Pointcut(value = "execution(* com.observer.domain.external..*.*(..))")
	public void externalLogAdvice() {}

	@Around("externalLogAdvice()")
	public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		String[] splitByDot = signature.getDeclaringTypeName().split("\\.");
		String daoName = splitByDot[splitByDot.length - 1];

		String methodName = signature.getName();
		String[] methodNameSplit = methodName.split("(?=[A-Z])");
		StringBuilder sb = new StringBuilder();
		for (String s : methodNameSplit) {
			sb.append(s).append(" ");
		}
		methodName = sb.toString().trim().toLowerCase();

		Object[] args = joinPoint.getArgs();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.debug("{} execute {} with {}", daoName, methodName, args);

		Object proceed = joinPoint.proceed();

		stopWatch.stop();
		log.debug("{} execute {} finished in {}ms", daoName, methodName, stopWatch.getTime());
		return proceed;
	}
}
