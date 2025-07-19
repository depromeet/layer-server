package org.layer.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.layer.tmpadmin.cache.MemberActivityCache;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MemberActivityAspect {

	private final MemberActivityCache memberActivityCache;

	@Around("execution(* org.layer.domain..controller..*(..))")
	public Object trackMemberActivity(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		String summary = "unknown";

		Operation operation = method.getAnnotation(Operation.class);
		if (operation != null && !operation.summary().isBlank()) {
			summary = operation.summary();
		} else {
			// 구현체에 없으면 인터페이스에서 @Operation 찾기
			Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
			for (Class<?> iface : interfaces) {
				try {
					Method interfaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
					Operation interfaceOp = interfaceMethod.getAnnotation(Operation.class);
					if (interfaceOp != null && !interfaceOp.summary().isBlank()) {
						summary = interfaceOp.summary();
						break;
					}
				} catch (NoSuchMethodException ignored) {
					// 메서드가 없을 수도 있으니 무시
				}
			}
		}

		Long memberId = extractMemberId(signature, joinPoint.getArgs());

		if (memberId != null) {
			memberActivityCache
				.activityMap
				.computeIfAbsent(memberId, id -> new ConcurrentHashMap<>())
				.merge(summary, 1, Integer::sum);

			log.info("📊 User [{}] called API '{}'", memberId, summary);
		}

		return joinPoint.proceed();
	}

	private Long extractMemberId(MethodSignature signature, Object[] args) {
		Annotation[][] paramAnnotations = signature.getMethod().getParameterAnnotations();

		for (int i = 0; i < paramAnnotations.length; i++) {
			for (Annotation annotation : paramAnnotations[i]) {
				if (annotation.annotationType().getSimpleName().equals("MemberId")) {
					return (Long) args[i];
				}
			}
		}
		return null;
	}
}