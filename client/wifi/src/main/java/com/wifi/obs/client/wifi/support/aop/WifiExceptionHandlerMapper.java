package com.wifi.obs.client.wifi.support.aop;

import com.wifi.obs.client.wifi.support.aop.annotation.WifiClientAdvice;
import com.wifi.obs.client.wifi.support.aop.annotation.WifiClientExceptionHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WifiExceptionHandlerMapper {

	private final ApplicationContext context;

	public void handleClientConnectionException(Throwable ex) {
		invokeAdvice(new Exception(ex));
	}

	private void invokeAdvice(Exception ex) {
		Map<String, Object> adviceBeans = getAdviceBeans();

		adviceBeans.forEach((k, v) -> executeHandler(v, ex));
	}

	/** Advice 어노테이션이 선언된 빈 가지고 오기 */
	private Map<String, Object> getAdviceBeans() {
		return context.getBeansWithAnnotation(WifiClientAdvice.class);
	}

	/** Advice 어노테이션이 선언된 빈을 대상으로 Handler 실행 */
	private void executeHandler(Object bean, Exception ex) {
		Method[] methods = bean.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (isHandlerMethod(method)) {
				invokeMethodIfApplicable(method, bean, ex);
			}
		}
	}

	/** 빈의 메서드 중 Handler 어노테이션 선언된 메서드 찾기 */
	private boolean isHandlerMethod(Method method) {
		return method.isAnnotationPresent(WifiClientExceptionHandler.class);
	}

	private void invokeMethodIfApplicable(Method method, Object bean, Exception ex) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		HashSet<Class<? extends Throwable>> exceptions = getExceptionsHandledByMethod(method);

		if (isMethodApplicable(parameterTypes, exceptions, ex)) {
			invokeMethod(method, bean, ex);
		}
	}

	/** 선언된 Handler 어노테이선에 포함된 Exception 목록 가지고 오기 */
	private HashSet<Class<? extends Throwable>> getExceptionsHandledByMethod(Method method) {
		return new HashSet<>(
				Arrays.asList(method.getAnnotation(WifiClientExceptionHandler.class).value()));
	}

	/** 얻어온 파리미터의 타입의 길이가 1인지 그리고 할당 가능한지 확인 */
	private boolean isMethodApplicable(
			Class<?>[] parameterTypes, HashSet<Class<? extends Throwable>> exceptions, Exception ex) {
		return parameterTypes.length == 1
				&& parameterTypes[0].isAssignableFrom(ex.getClass())
				&& exceptions.contains(ex.getClass());
	}

	private void invokeMethod(Method method, Object bean, Exception ex) {
		try {
			method.invoke(bean, ex);
		} catch (Exception e) {
			log.error("Exception occurred while invoking method: {}", method.getName(), e);
		}
	}
}
