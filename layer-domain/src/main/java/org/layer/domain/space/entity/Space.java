package org.layer.domain.space.entity;

import static org.layer.global.exception.SpaceExceptionType.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.common.BaseTimeEntity;
import org.layer.domain.space.exception.SpaceException;


@Getter
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Space extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bannerUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SpaceCategory category;

    @NotNull
    private String name;

    private String introduction;

    @NotNull
    private Long leaderId;

    /**
     * Form Relationid
     */
    private Long formId;

    public void isLeaderSpace(Long memberId) {
        boolean isLeader = leaderId.equals(memberId);
        if (!isLeader) {
            throw new SpaceException(CAN_ONLY_SPACE_LEADER);
        }
    }

    public void changeLeader(Long memberId) {
        if (leaderId.equals(memberId)) {
            throw new SpaceException(SPACE_LEADER_NOT_ALLOW);
        }
        leaderId = memberId;
    }

    public void updateRecentFormId(Long formId, Long memberId){
        isLeaderSpace(memberId);
        this.formId = formId;
    }

    public void updateSpace(Long memberId, String bannerUrl, String name, String introduction){
        isLeaderSpace(memberId);
        this.bannerUrl = bannerUrl;
        this.name = name;
        this.introduction = introduction;
    }

    public void updateBannerUrl(String bannerUrl){
        this.bannerUrl = bannerUrl;
    }

    @Builder
    public Space(String bannerUrl, SpaceCategory category, String name, String introduction,
        Long leaderId, Long formId) {
        this.bannerUrl = bannerUrl;
        this.category = category;
        this.name = name;
        this.introduction = introduction;
        this.leaderId = leaderId;
        this.formId = formId;
    }
}
