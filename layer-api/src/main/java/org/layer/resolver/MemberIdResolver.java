package org.layer.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.MemberId;
import org.layer.common.exception.BaseCustomException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static org.layer.common.exception.MemberExceptionType.UNAUTHORIZED_USER;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberIdResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 1. 어노테이션 체크
        var annotation = parameter.hasParameterAnnotation(MemberId.class);

        // 2. 파라미터의 타입체크
        var parameterType = parameter.getParameterType().equals(Long.class);

        return (annotation && parameterType);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            var securityContext = SecurityContextHolder.getContext();
            var memberId = Optional.ofNullable(securityContext).map(it -> it.getAuthentication().getName()).orElseThrow(() -> new BaseCustomException(UNAUTHORIZED_USER));
            return Long.parseLong(memberId);
        } catch (Exception e) {
            throw new BaseCustomException(UNAUTHORIZED_USER);
        }
    }
}
