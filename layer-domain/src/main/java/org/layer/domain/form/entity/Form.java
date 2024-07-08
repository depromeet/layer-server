package org.layer.domain.form.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.layer.domain.BaseEntity;
import org.layer.domain.form.converter.FormPublishedByConverter;
import org.layer.domain.form.enums.FormPublishedBy;


@Getter
@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseEntity {

    private Long memberId;

    @NotNull
    private String title;

    @Column(length = 10)
    @ColumnDefault("1")
    @Convert(converter = FormPublishedByConverter.class)
    @NotNull
    private FormPublishedBy formPublishedBy;

    private String introduction;

}
