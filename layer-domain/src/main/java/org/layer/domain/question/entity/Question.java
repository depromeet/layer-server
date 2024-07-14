package org.layer.domain.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.BaseEntity;
import org.layer.domain.question.converter.QuestionTypeConverter;
import org.layer.domain.question.enums.QuestionOwner;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.questionOption.entity.QuestionOption;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @NotNull
    private Long questionOwnerId;

    @NotNull
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionOwner questionOwner;

    @Column(length = 20)
    @NotNull
    @Convert(converter = QuestionTypeConverter.class)
    private QuestionType questionType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionOption> options = new HashSet<>();

    public Question(Long questionOwnerId, String content, QuestionOwner questionOwner, QuestionType questionType) {
        this.questionOwnerId = questionOwnerId;
        this.content = content;
        this.questionOwner = questionOwner;
        this.questionType = questionType;
    }
}
