package org.layer.domain.form.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;


@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseEntity {

    /*
     * 해당 폼 생성한 멤버 id
     */
    @NotNull
    private Long memberId;

    private Long spaceId;

    @NotNull
    private String title;

    @NotNull
    private String introduction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FormType formType;

    public Form(Long memberId, Long spaceId, String title, String introduction, FormType formType) {
        this.memberId = memberId;
        this.spaceId = spaceId;
        this.title = title;
        this.introduction = introduction;
        this.formType = formType;
    }
}
