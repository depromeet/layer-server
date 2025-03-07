package org.layer.external.ai.service;

import static org.layer.domain.answer.entity.Answers.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.entity.AnalyzeDetail;
import org.layer.domain.analyze.enums.AnalyzeDetailType;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.repository.AnalyzeRepository;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.question.entity.Questions;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.AnalysisStatus;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.external.ai.dto.response.OpenAIResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAnalyzeService {
	private static final int FIRST_RANK = 1;
	private static final String RETROSPECT_LOCK_KEY = "retrospect:lock:";

	private final RetrospectRepository retrospectRepository;
	private final AnalyzeRepository analyzeRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;

	private final OpenAIService openAIService;

	private final RedisTemplate<String, Object> redisTemplate;

	@Transactional
	@Async
	public void createAnalyze(Long spaceId, Long retrospectId, List<Long> memberIds) {
		String lockKey = RETROSPECT_LOCK_KEY + retrospectId;
		String lockValue = UUID.randomUUID().toString();
		boolean lockAcquired = false;

		try {
			// 1. Redis에 Lock 설정 (SETNX 방식)
			lockAcquired = Boolean.TRUE.equals(redisTemplate.opsForValue()
				.setIfAbsent(lockKey, lockValue, Duration.ofSeconds(15))); // 15초 TTL

			if (!lockAcquired) {
				log.info("Another process is already handling retrospectId: {}", retrospectId);
				return;
			}

			long startTime = System.currentTimeMillis();
			log.info("createAnalyze started for retrospectId: {}", retrospectId);

			// 해당 스페이스 팀원인지 검증
			Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
			memberIds.forEach(team::validateTeamMembership);

			// 회고 마감 여부 확인
			Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
			retrospect.validateAnalysisStatusIsNotDone();

			// 답변 조회
			Questions questions = new Questions(questionRepository.findAllByRetrospectIdOrderByQuestionOrder(retrospectId));
			Answers answers = new Answers(answerRepository.findAllByRetrospectIdAndAnswerStatus(retrospectId, AnswerStatus.DONE));

			Long rangeQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.RANGER);
			Long numberQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.NUMBER);
			String totalAnswer = answers.getTotalAnswer(rangeQuestionId, numberQuestionId);

			// 분석 요청
			List<Analyze> analyzes = new ArrayList<>();

			OpenAIResponse aiResponse = openAIService.createAnalyze(totalAnswer);
			OpenAIResponse.Content content = aiResponse.parseContent();

			Analyze teamAnalyze = getAnalyzeEntity(retrospectId, answers, rangeQuestionId, numberQuestionId, content, null, AnalyzeType.TEAM);
			analyzes.add(teamAnalyze);

			List<Analyze> individualAnalyzes = memberIds.stream()
				.map(memberId -> {
					String individualAnswer = answers.getIndividualAnswer(rangeQuestionId, numberQuestionId, memberId);
					OpenAIResponse aiIndividualResponse = openAIService.createAnalyze(individualAnswer);
					OpenAIResponse.Content individualContent = aiIndividualResponse.parseContent();
					return getAnalyzeEntity(retrospectId, answers, rangeQuestionId, numberQuestionId, individualContent, memberId, AnalyzeType.INDIVIDUAL);
				})
				.toList();
			analyzes.addAll(individualAnalyzes);

			analyzeRepository.saveAll(analyzes);

			long endTime = System.currentTimeMillis();
			log.info("createAnalyze completed in {} ms", (endTime - startTime));

			// 회고 분석 상태 업데이트 (Dirty Checking 안 될 경우 save 호출)
			retrospect.updateAnalysisStatus(AnalysisStatus.DONE);
			retrospectRepository.save(retrospect);

		} catch (Exception e) {
			log.error("Error in createAnalyze: {}", e.getMessage(), e);
		} finally {
			// 2. Redis Lock 해제 (현재 락을 가진 주체만 삭제)
			if (lockAcquired) {
				String currentLockValue = (String)redisTemplate.opsForValue().get(lockKey);
				if (lockValue.equals(currentLockValue)) {
					redisTemplate.delete(lockKey);
				}
			}
		}
	}

	private Analyze getAnalyzeEntity(Long retrospectId, Answers answers, Long rangeQuestionId, Long numberQuestionId,
		OpenAIResponse.Content content, Long memberId, AnalyzeType analyzeType) {
		List<AnalyzeDetail> analyzeDetails = createAnalyzeDetails(content);

		return createAnalyzeEntity(retrospectId, memberId, answers.getScoreCount(numberQuestionId, SCORE_ONE, memberId),
			answers.getScoreCount(numberQuestionId, SCORE_TWO, memberId), answers.getScoreCount(numberQuestionId, SCORE_THREE, memberId),
			answers.getScoreCount(numberQuestionId, SCORE_FOUR, memberId), answers.getScoreCount(numberQuestionId, SCORE_FIVE, memberId),
			answers.getGoalCompletionRate(rangeQuestionId), analyzeType, analyzeDetails);
	}

	private List<AnalyzeDetail> createAnalyzeDetails(OpenAIResponse.Content content) {
		List<AnalyzeDetail> analyzeDetails = new ArrayList<>();

		analyzeDetails.addAll(createAnalyzeDetail(content.getGoodPoints(), AnalyzeDetailType.GOOD));
		analyzeDetails.addAll(createAnalyzeDetail(content.getBadPoints(), AnalyzeDetailType.BAD));
		analyzeDetails.addAll(createAnalyzeDetail(content.getImprovementPoints(), AnalyzeDetailType.IMPROVEMENT));
		analyzeDetails.addAll(createAnalyzeDetail(content.getImprovementPoints(), AnalyzeDetailType.FREQUENCY));

		return analyzeDetails;
	}

	private List<AnalyzeDetail> createAnalyzeDetail(List<OpenAIResponse.ContentDetail> contentDetails,
		AnalyzeDetailType analyzeDetailType) {

		List<AnalyzeDetail> analyzeDetails = new ArrayList<>();
		int rank = FIRST_RANK;

		for (OpenAIResponse.ContentDetail detail : contentDetails) {
			AnalyzeDetail analyzeDetail = AnalyzeDetail.builder()
				.content(detail.getPoint())
				.count(detail.getCount())
				.rank(rank)
				.analyzeDetailType(analyzeDetailType)
				.build();
			analyzeDetails.add(analyzeDetail);
			rank++;
		}

		return analyzeDetails;
	}

	private Analyze createAnalyzeEntity(Long retrospectId, Long memberId,int scoreOne, int scoreTwo, int scoreThree, int scoreFour,
		int scoreFive, int goalCompletionRate, AnalyzeType analyzeType, List<AnalyzeDetail> analyzeDetails) {

		return Analyze.builder()
			.retrospectId(retrospectId)
			.memberId(memberId)
			.scoreOne(scoreOne)
			.scoreTwo(scoreTwo)
			.scoreThree(scoreThree)
			.scoreFour(scoreFour)
			.scoreFive(scoreFive)
			.goalCompletionRate(goalCompletionRate)
			.analyzeType(analyzeType)
			.analyzeDetails(analyzeDetails)
			.build();
	}
}
