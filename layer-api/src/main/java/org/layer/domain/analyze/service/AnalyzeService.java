package org.layer.domain.analyze.service;

import java.util.ArrayList;
import java.util.List;

import org.layer.domain.analyze.controller.dto.response.AnalyzeGetResponse;
import org.layer.domain.analyze.entity.Analyze;
import org.layer.domain.analyze.entity.AnalyzeDetail;
import org.layer.domain.analyze.enums.AnalyzeType;
import org.layer.domain.analyze.repository.AnalyzeRepository;
import org.layer.domain.answer.entity.Answers;
import org.layer.domain.answer.repository.AnswerRepository;
import org.layer.domain.question.entity.Questions;
import org.layer.domain.question.enums.QuestionType;
import org.layer.domain.question.repository.QuestionRepository;
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Space;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.external.ai.dto.response.OpenAIResponse;
import org.layer.external.ai.service.OpenAIService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyzeService {

	private static final int FIRST_RANK = 1;

	private final SpaceRepository spaceRepository;
	private final RetrospectRepository retrospectRepository;
	private final AnalyzeRepository analyzeRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;

	private final OpenAIService openAIService;

	@Transactional
	public void createAnalyze(Long spaceId, Long retrospectId, Long memberId) {
		// 리더인지 확인
		Space space = spaceRepository.findByIdOrThrow(spaceId);
		space.isLeaderSpace(memberId);

		// 회고 마감 여부 확인
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.validateRetrospectStatusDone();

		// 답변 조회
		Questions questions = new Questions(questionRepository.findAllByRetrospectIdOrderByQuestionOrder(retrospectId));
		Answers answers = new Answers(answerRepository.findAllByRetrospectId(retrospectId));
		String totalAnswer = answers.getTotalAnswer(
			questions.extractEssentialQuestionIdBy(QuestionType.RANGER),
			questions.extractEssentialQuestionIdBy(QuestionType.NUMBER));
		// 분석 요청
		OpenAIResponse aiResponse = openAIService.createAnalyze(totalAnswer);
		OpenAIResponse.Content content = aiResponse.parseContent();


		Long numberQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.NUMBER);
		Long rangeQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.RANGER);

		List<AnalyzeDetail> analyzeDetails = createAnalyzeDetails(content);
		Analyze analyze = createAnalyze(retrospectId, answers.getSatisfactionCount(rangeQuestionId),
			answers.getNormalCount(rangeQuestionId), answers.getRegretCount(rangeQuestionId),
			answers.getGoalCompletionRate(numberQuestionId),
			analyzeDetails);

		analyzeRepository.save(analyze);
	}

	public AnalyzeGetResponse getAnalyze(Long spaceId, Long retrospectId, Long memberId) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		Analyze analyze = analyzeRepository.findByRetrospectIdOrThrow(retrospectId);

		return AnalyzeGetResponse.of(analyze);
	}

	private List<AnalyzeDetail> createAnalyzeDetails(OpenAIResponse.Content content) {
		List<AnalyzeDetail> analyzeDetails = new ArrayList<>();

		analyzeDetails.addAll(createAnalyzeDetail(content.getGoodPoints(), AnalyzeType.GOOD));
		analyzeDetails.addAll(createAnalyzeDetail(content.getBadPoints(), AnalyzeType.BAD));
		analyzeDetails.addAll(createAnalyzeDetail(content.getImprovementPoints(), AnalyzeType.IMPROVEMENT));

		return analyzeDetails;
	}

	private List<AnalyzeDetail> createAnalyzeDetail(List<OpenAIResponse.ContentDetail> contentDetails,
		AnalyzeType analyzeType) {

		List<AnalyzeDetail> analyzeDetails = new ArrayList<>();
		int rank = FIRST_RANK;

		for(OpenAIResponse.ContentDetail detail : contentDetails){
			AnalyzeDetail analyzeDetail = AnalyzeDetail.builder()
				.content(detail.getPoint())
				.count(detail.getCount())
				.rank(rank)
				.analyzeType(analyzeType)
				.build();
			analyzeDetails.add(analyzeDetail);
			rank++;
		}

		return analyzeDetails;
	}

	private Analyze createAnalyze(Long retrospectId, int satisfactionCount, int normalCount, int regretCount,
		int goalCompletionRate, List<AnalyzeDetail> analyzeDetails) {

		return Analyze.builder()
			.retrospectId(retrospectId)
			.satisfactionCount(satisfactionCount)
			.normalCount(normalCount)
			.regretCount(regretCount)
			.goalCompletionRate(goalCompletionRate)
			.analyzeDetails(analyzeDetails)
			.build();
	}

}
