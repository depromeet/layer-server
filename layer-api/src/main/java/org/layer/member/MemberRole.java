package org.layer.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum MemberRole implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}