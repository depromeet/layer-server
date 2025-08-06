package org.layer.aop;

import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class ExecutionLoggingAop {

	// 모든 layer-api 모듈 내의 controller package에 존재하는 클래스
	@Around("execution(* org.layer.domain..controller..*(..))")
	public Object logExecutionTrace(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long memberId = null;
		if (!authentication.getPrincipal().equals("anonymousUser")) {
			memberId = (Long) authentication.getPrincipal();
		}

		log.info("[Request URI] : " + request.getRequestURI());

		String className = pjp.getSignature().getDeclaringType().getSimpleName();
		String methodName = pjp.getSignature().getName();
		String task = className + "." + methodName;
		log.info("[Call Method] " + httpMethod + ": " + task + " | Request userId=" + memberId);

		Object[] paramArgs = pjp.getArgs();
		String loggingMessage = "";
		int cnt = 1;
		for (Object object : paramArgs) {
			if (Objects.nonNull(object)) {
				String paramName = "[param" + cnt +"] " + object.getClass().getSimpleName();
				String paramValue = " [value" + cnt +"] " + object;
				loggingMessage += paramName + paramValue + "\n";
				cnt++;
			}
		}
		log.info("{}", loggingMessage);
		// 해당 클래스 처리 전의 시간
		StopWatch sw = new StopWatch();
		sw.start();

		Object result = null;

		// 해당 클래스의 메소드 실행
		result = pjp.proceed();

		// 해당 클래스 처리 후의 시간
		sw.stop();
		long executionTime = sw.getTotalTimeMillis();

		log.info("[ExecutionTime] " + task + " --> " + executionTime + " (ms)");

		return result;
	}

}
