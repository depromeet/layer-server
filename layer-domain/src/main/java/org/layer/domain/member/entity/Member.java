package org.layer.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @NotNull
    private String socialId;

    private String profileImageUrl;


    @Builder(access = AccessLevel.PUBLIC)
    private Member(String name, String email, MemberRole memberRole,
                   SocialType socialType, String socialId) {
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
