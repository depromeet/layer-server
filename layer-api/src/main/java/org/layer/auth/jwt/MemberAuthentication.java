package org.layer.auth.jwt;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.layer.member.MemberRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    @Builder
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication create(Object principal, MemberRole role) {
        return MemberAuthentication.builder()
                .principal(principal)
                .credentials(null)
                .authorities(Collections.singleton(role))
                .build();
    }

}
