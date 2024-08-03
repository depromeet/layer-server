package org.layer.domain.answer.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.layer.domain.answer.exception.AnswerException;

import java.util.List;

import static org.layer.common.exception.AnswerExceptionType.*;

@RequiredArgsConstructor
@Getter
public class Answers {
    private static final int ZERO = 0;

    private final List<Answer> answers;

    public String getAnswerToQuestion(Long questionId) {
        return answers.stream()
                .filter(answer -> answer.getQuestionId().equals(questionId))
                .map(Answer::getContent)
                .findFirst()
                .orElse(null);
    }

    public boolean hasRetrospectAnswer(Long memberId, Long retrospectId) {
        return answers.stream()
                .filter(answer -> answer.getRetrospectId().equals(retrospectId))
                .anyMatch(answer -> answer.getMemberId().equals(memberId));
    }

    public long getWriteCount(Long retrospectId) {
        return answers.stream()
                .filter(answer -> answer.getRetrospectId().equals(retrospectId))
                .count();
    }

    public void validateNoAnswer() {
        if (answers.size() != ZERO) {
            throw new AnswerException(ALREADY_ANSWERED);
        }
    }

    public void validateContainAnswers() {
        if (answers.size() == ZERO) throw new AnswerException(NOT_CONTAIN_ANSWERS);
    }


    public void validateAlreadyAnswer(Long memberId, Long retrospectId) {
        if (!hasRetrospectAnswer(memberId, retrospectId)) {
            throw new AnswerException(NOT_ANSWERED);
        }
    }

}
