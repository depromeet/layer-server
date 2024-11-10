package org.layer.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.layer.domain.answer.entity.Answer;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.common.time.Time;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.entity.RetrospectStatus;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.external.ai.service.AIAnalyzeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetrospectScheduler {

    private final RetrospectRepository retrospectRepository;
    private final AnswerRepository answerRepository;
    private final AIAnalyzeService aiAnalyzeService;

    private final Time time;

    /**
     * @note: 1시간마다 실행된다.
     */
    @Scheduled(cron = "0 0/2 * * * *")
    public void updateRetrospectStatusToDone() {
        log.info("Batch Start : updateRetrospectStatusToDone");

        LocalDateTime now = time.now();

        List<Retrospect> retrospects = retrospectRepository.findAllByDeadlineBeforeAndRetrospectStatus(
                now, RetrospectStatus.PROCEEDING);

        log.info("batch: size of retrospects: {}", retrospects.size());

        Map<Long, Retrospect> retrospectMap = retrospects.stream()
                .collect(Collectors.toMap(Retrospect::getId, retrospect -> retrospect));

        retrospects.forEach(retrospect -> retrospect.updateRetrospectStatus(RetrospectStatus.DONE));
        retrospectRepository.saveAllAndFlush(retrospects);

        List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
        Answers totalAnswers = new Answers(answerRepository.findAllByRetrospectIdIn(retrospectIds));

        Map<Long, List<Answer>> answerMap = totalAnswers.getAnswers().stream()
                .collect(Collectors.groupingBy(Answer::getRetrospectId));

        // for 문 돌기
        answerMap.keySet().forEach(retrospectId -> {
            Retrospect retrospect = retrospectMap.get(retrospectId);
            Answers answers = new Answers(answerMap.get(retrospectId));
            aiAnalyzeService.createAnalyze(retrospect.getSpaceId(), retrospectId, answers.getWriteMemberIds());
        });

        log.info("Batch End : updateRetrospectStatusToDone");
    }
}