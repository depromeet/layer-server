package org.layer.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Aspect
@Component
@Log4j2
public class RecentAccessHistoryAop {
    private final RedisTemplate<String, String> redisTemplate;

    // 모든 layer-api 모듈 내의 controller package에 존재하는 클래스
    @Around("execution(* org.layer.domain..controller..*(..))")
    public Object recordRecentAccessHistory(ProceedingJoinPoint pjp) throws Throwable {
        Long memberId = getCurrentMemberId();

        if(memberId != null) { // 멤버 아이디가 있다면 현재 시간을 저장.
            setRecentTime(Long.toString(memberId), LocalDateTime.now().toString());
        }
        Object result = pjp.proceed();
        return result;
    }

    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return Long.parseLong(authentication.getName());
        } catch(Exception e) {
            return null;
        }
    }


    private void setRecentTime(String memberId, String recentTime) {
        Duration ttl = Duration.ofDays(30 * 6); // 6개월
        redisTemplate.opsForValue().set(memberId, recentTime, ttl);
    }
}