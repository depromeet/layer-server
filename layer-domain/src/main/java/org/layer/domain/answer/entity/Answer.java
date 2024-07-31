package org.layer.domain.answer.entity;

import org.layer.domain.answer.enums.AnswerStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

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
}
