package org.layer.domain.retrospect.entity;

import org.layer.domain.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospect extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long spaceId;

    @NotNull
    private Long formId;

    @NotNull
    private String title;

    @NotNull
    private String introduction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RetrospectStatus retrospectStatus;

    @Builder
    public Retrospect(Long spaceId, Long formId, String title, String introduction, RetrospectStatus retrospectStatus) {
        this.spaceId = spaceId;
        this.formId = formId;
        this.title = title;
        this.introduction = introduction;
        this.retrospectStatus = retrospectStatus;
    }
}
