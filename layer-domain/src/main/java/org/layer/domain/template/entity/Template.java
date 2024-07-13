package org.layer.domain.template.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Template extends BaseEntity {
    @NotNull
    private Long id;

    @NotNull
    private String title; // ex. 빠르고 효율적인 회고

    @NotNull
    private String templateName; // ex. KPT 회고

    @NotNull
    private String templateImageUrl;

    //== 회고 설명에 대한 부분 ==//
    @NotNull
    private String description; // 회고에 대한 설명

    @NotNull
    private String tipTitle; // ex) 회고는 빠르고 간단하게!

    @NotNull
    private String tipDescription; // 팁에 대한 설명
}
