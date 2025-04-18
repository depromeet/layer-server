package org.layer.domain.questionOption.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;
import org.layer.domain.question.entity.Question;

@Entity
@Table(name = "question_option", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"question_id", "option_value"})
})
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionLabel;

    @NotNull
    private String optionValue;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}