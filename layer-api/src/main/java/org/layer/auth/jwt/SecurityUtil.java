package org.layer.auth.jwt;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionType;
import org.layer.common.exception.MemberExceptionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.layer.common.exception.MemberExceptionType.UNAUTHORIZED_USER;

@Component
public class SecurityUtil {
    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            return Long.parseLong(authentication.getName());
        } catch(Exception e) {
            throw new BaseCustomException(UNAUTHORIZED_USER);
        }
    }
}
