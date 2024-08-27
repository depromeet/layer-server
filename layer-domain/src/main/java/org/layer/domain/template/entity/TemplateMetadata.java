package org.layer.domain.template.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TemplateMetadata extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long formId;

    // @NotNull
    private String templateImageUrl;

    //== 회고 설명에 대한 부분 ==//
    @NotNull
    private String tipTitle; // ex) 회고는 빠르고 간단하게!

    @NotNull
    private String tipDescription; // 팁에 대한 설명. ex)
}
