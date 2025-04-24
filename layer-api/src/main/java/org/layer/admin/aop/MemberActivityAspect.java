package org.layer.admin.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.layer.admin.cache.MemberActivityCache;
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

	@Pointcut("@annotation(io.swagger.v3.oas.annotations.Operation)")
	public void operationAnnotatedMethod() {}

	@Around("operationAnnotatedMethod()")
	public Object trackMemberActivity(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Operation operation = method.getAnnotation(Operation.class);
		String summary = operation != null ? operation.summary() : "unknown";

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



