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

    @NotNull
    private String templateName; // ex. KPT 회고

    // @NotNull
    private String templateImageUrl;

    private String firstTag; // 첫번째 태그. ex) 간단한 프로세스

    private String secondTag; // 두번째 태그. ex) 다음 목표 설정

    //== 회고 설명에 대한 부분 ==//

    @NotNull
    private String tipTitle; // ex) 회고는 빠르고 간단하게!

    @NotNull
    private String tipDescription; // 팁에 대한 설명. ex)
}
