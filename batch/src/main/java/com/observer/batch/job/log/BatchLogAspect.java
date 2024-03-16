package com.observer.batch.job.log;

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
public class BatchLogAspect {

	@Pointcut(value = "execution(* com.observer.batch.job..*.process(..))")
	public void processAdvice() {}

	@Pointcut(value = "execution(* com.observer.batch.job..*.write(..))")
	public void writeAdvice() {}

	@Pointcut(value = "execution(* com.observer.batch.job..*.execute(..))")
	public void serviceAdvice() {}

	@Around("processAdvice() || writeAdvice() || serviceAdvice()")
	public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		String[] splitByDot = signature.getDeclaringTypeName().split("\\.");
		String serviceName = splitByDot[splitByDot.length - 1];

		Object[] args = joinPoint.getArgs();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.debug("{} execute with {}", serviceName, args);

		Object proceed = joinPoint.proceed();

		stopWatch.stop();
		log.debug("{} finished in {}ms", serviceName, stopWatch.getTime());
		return proceed;
	}
}
