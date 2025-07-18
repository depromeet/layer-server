package org.layer.admin.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.layer.admin.member.entity.AdminMemberRole;
import org.layer.admin.member.entity.AdminMemberSignupHistory;
import org.layer.admin.member.repository.AdminMemberRepository;
import org.layer.event.BaseEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class UserOnlyEventListenerAspect {

	private final AdminMemberRepository adminMemberRepository;

	@Around("@annotation(UserOnlyEventListener) && @annotation(org.springframework.context.event.EventListener)")
	public Object skipIfAdminRole(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args.length == 0) return joinPoint.proceed();

		if (!(args[0] instanceof BaseEvent event)) {
			return joinPoint.proceed();
		}

		Long memberId = event.memberId();
		if (memberId == null) return joinPoint.proceed();

		AdminMemberRole role = adminMemberRepository.findById(memberId)
			.map(AdminMemberSignupHistory::getMemberRole)
			.orElse(null);

		if (role == AdminMemberRole.ADMIN) {
			// 🔥 ADMIN이면 스킵
			return null;
		}

		return joinPoint.proceed();
	}
}
