package org.layer.domain.retrospect.dto;

import lombok.*;
import org.layer.domain.member.entity.Member;
import org.layer.domain.space.entity.Space;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRetrospectCountGroupBySpaceGetResponse {
    private Long spaceId;
    private String spaceName;
    private LocalDateTime spaceCreatedAt;
    private Long leaderId;
    private String leaderEmail;
    private String leaderName;
    private Long retrospectCount;
    public AdminRetrospectCountGroupBySpaceGetResponse of(Space space, Member leader, Long retrospectCount) {
        return AdminRetrospectCountGroupBySpaceGetResponse.builder()
                .spaceId(space.getId())
                .spaceName(space.getName())
                .leaderId(leader.getId())
                .leaderEmail(leader.getEmail())
                .spaceCreatedAt(space.getCreatedAt())
                .leaderName(leader.getName())
                .retrospectCount(retrospectCount)
                .build();
    }

    public AdminRetrospectCountGroupBySpaceGetResponse(Space space, Member member, Long retrospectCount) {
        this.spaceId = space.getId();
        this.spaceName = space.getName();
        this.spaceCreatedAt = space.getCreatedAt();
        this.leaderId = member.getId();
        this.leaderName = member.getName();
        this.leaderEmail = member.getEmail();
        this.retrospectCount = retrospectCount;
    }
}
