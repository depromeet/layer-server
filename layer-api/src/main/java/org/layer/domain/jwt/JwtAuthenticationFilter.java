package org.layer.domain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.member.entity.MemberRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getJwtFromRequest(request);

        if(jwtValidator.isValidToken(accessToken)) {
            Long memberId = jwtValidator.getMemberIdFromToken(accessToken);
            List<String> role = jwtValidator.getRoleFromToken(accessToken);
            setAuthenticationToContext(memberId, MemberRole.valueOf(role.get(0)));
        }
        filterChain.doFilter(request, response);
    }

    // Spring Security Context에 저장
    private void setAuthenticationToContext(Long memberId, MemberRole memberRole) {
        Authentication authentication = MemberAuthentication.create(memberId, memberRole);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 요청 헤더에서 액세스 토큰을 가져오는 메서드
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String accessToken = null;
        if(StringUtils.hasText(bearerToken)) {
            accessToken = bearerToken.replace("Bearer ", "");
        }
        return accessToken;
    }

    // 정상적인 토큰인지 판단하는 메서드
    private boolean isValidToken(String token) {
        return StringUtils.hasText(token) && jwtValidator.validateToken(token) == JwtValidationType.VALID_JWT;
    }
}
