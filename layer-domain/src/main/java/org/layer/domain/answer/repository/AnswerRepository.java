package org.layer.domain.answer.repository;

import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.enums.AnswerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByRetrospectIdIn(List<Long> retrospectIds);

    List<Answer> findAllByRetrospectId(Long retrospectId);

    List<Answer> findByRetrospectIdAndMemberIdAndAnswerStatusAndQuestionIdIn(Long retrospectId, Long memberId,
                                                                             AnswerStatus answerStatus, List<Long> questionId);

    List<Answer> findAllByRetrospectIdAndAnswerStatus(Long retrospectId, AnswerStatus answerStatus);

    List<Answer> findAllByRetrospectIdAndMemberIdAndAnswerStatus(Long retrospectId, Long memberId,
                                                                 AnswerStatus answerStatus);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Answer a WHERE a.retrospectId = :retrospectId AND a.memberId = :memberId AND a.answerStatus = :answerStatus")
    void deleteAllByRetrospectIdAndMemberIdAndAnswerStatus(Long retrospectId, Long memberId, AnswerStatus answerStatus);
}
