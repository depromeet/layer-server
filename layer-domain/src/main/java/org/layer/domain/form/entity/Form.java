package org.layer.domain.form.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.layer.domain.BaseEntity;


@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseEntity {

    @NotNull
    private Long memberId;

    @NotNull
    private String title;

    @NotNull
    private String introduction;

}
