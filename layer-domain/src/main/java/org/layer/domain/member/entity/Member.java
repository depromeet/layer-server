package org.layer.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private MemberRole memberRole;

    @NotNull
    private SocialType socialType;

    @NotNull
    private String socialId;


    @Builder(access = AccessLevel.PUBLIC)
    private Member(String name,String email, MemberRole memberRole,
                   SocialType socialType, String socialId) {
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
