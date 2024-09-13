package org.layer.domain.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SpaceMember {

    private final Long id;
    private final String avatar;

    private final String name;

    private final Boolean isLeader;
    private final LocalDateTime deletedAt;

    @QueryProjection
    public SpaceMember(Long id, String avatar, String name, Boolean isLeader, LocalDateTime deletedAt) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.isLeader = isLeader;
        this.deletedAt = deletedAt;
    }
}
