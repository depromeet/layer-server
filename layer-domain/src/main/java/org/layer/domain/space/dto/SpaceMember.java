package org.layer.domain.space.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SpaceMember {

    private final Long id;
    private final String avatar;

    private final String name;

    private final Boolean isLeader;
    private final String delYn;

    @QueryProjection
    public SpaceMember(Long id, String avatar, String name, Boolean isLeader, String delYn) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.isLeader = isLeader;
        this.delYn = delYn;
    }
}
