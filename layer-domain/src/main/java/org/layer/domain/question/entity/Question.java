package org.layer.domain.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.common.BaseTimeEntity;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.questionOption.entity.QuestionOption;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        formId 나 retrospectId 둘 중 하나가 null 이고, 하나는 값이 지정되어야 한다.
    */
    private Long formId;

    private Long retrospectId;

    @NotNull
    private String content;

    private int questionOrder;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionOwner questionOwner;

    @Column(length = 20)
    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionOption> options = new HashSet<>();

    @Builder
    private Question(Long formId, Long retrospectId, String content, int questionOrder, QuestionOwner questionOwner, QuestionType questionType) {
        this.formId = formId;
        this.retrospectId = retrospectId;
        this.content = content;
        this.questionOrder = questionOrder;
        this.questionOwner = questionOwner;
        this.questionType = questionType;
    }
}
