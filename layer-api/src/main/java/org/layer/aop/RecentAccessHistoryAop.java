package org.layer.aop;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.layer.common.dto.RecentActivityDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Aspect
@Component
@Profile("!test")
public class RecentAccessHistoryAop {
    // @Qualifier("recentActivityDate")
    // private final RedisTemplate<String, Object> redisTemplate;
    //
    // // 모든 layer-api 모듈 내의 controller package에 존재하는 클래스
    // @Around("execution(* org.layer.domain..controller..*(..))")
    // public Object recordRecentAccessHistory(ProceedingJoinPoint pjp) throws Throwable {
    //     Long memberId = getCurrentMemberId();
    //
    //     if(memberId != null) { // 멤버 아이디가 있다면 현재 시간을 저장.
    //         setRecentTime(Long.toString(memberId), LocalDateTime.now());
    //     }
    //     Object result = pjp.proceed();
    //     return result;
    // }
    //
    // private Long getCurrentMemberId() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //
    //     try {
    //         return Long.parseLong(authentication.getName());
    //     } catch(Exception e) {
    //         return null;
    //     }
    // }
    //
    //
    // private void setRecentTime(String memberId, LocalDateTime recentTime) {
    //     Duration ttl = Duration.ofDays(30 * 6); // 6개월
    //     redisTemplate.opsForValue().set(memberId, new RecentActivityDto(recentTime), ttl);
    // }
}