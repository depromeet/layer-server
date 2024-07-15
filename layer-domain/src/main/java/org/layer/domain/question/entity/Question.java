package org.layer.domain.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.BaseEntity;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.questionOption.entity.QuestionOption;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    /*
        questionOwnerId은 retrospectId or memberId 중 하나이다.
    */
    @NotNull
    private Long questionOwnerId;

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

    public Question(Long questionOwnerId, String content, int questionOrder, QuestionOwner questionOwner, QuestionType questionType) {
        this.questionOwnerId = questionOwnerId;
        this.content = content;
        this.questionOrder = questionOrder;
        this.questionOwner = questionOwner;
        this.questionType = questionType;
    }
}
