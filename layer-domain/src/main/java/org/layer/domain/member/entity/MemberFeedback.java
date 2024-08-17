package org.layer.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberFeedback {

    private Long memberId;
    private String memberName;
    private String email;
    private LocalDateTime memberCreatedAt;
    private Long retrospectCount;
    private Long spaceCount;

    @Override
    public String toString() {
        return "MemberDataDto{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", email='" + email + '\'' +
                ", memberCreatedAt=" + memberCreatedAt +
                ", retrospectCount=" + retrospectCount +
                ", spaceCount=" + spaceCount +
                '}';
    }
}