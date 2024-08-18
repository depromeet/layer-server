package org.layer.domain.answer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.layer.domain.answer.enums.AnswerStatus;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long retrospectId;

    @NotNull
    private Long questionId;

    @NotNull
    private Long memberId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;

    @Builder
    public Answer(Long retrospectId, Long questionId, Long memberId, String content, AnswerStatus answerStatus) {
        this.retrospectId = retrospectId;
        this.questionId = questionId;
        this.memberId = memberId;
        this.content = content;
        this.answerStatus = answerStatus;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
