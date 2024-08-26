package org.layer.domain.analyze.service;

import java.util.ArrayList;
import java.util.List;

import org.layer.domain.analyze.controller.dto.response.AnalyzeGetResponse;
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
import org.layer.domain.retrospect.entity.Retrospect;
import org.layer.domain.retrospect.repository.RetrospectRepository;
import org.layer.domain.space.entity.Team;
import org.layer.domain.space.repository.MemberSpaceRelationRepository;
import org.layer.domain.space.repository.SpaceRepository;
import org.layer.external.ai.dto.response.OpenAIResponse;
import org.layer.external.ai.service.OpenAIService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyzeService {

	private static final int FIRST_RANK = 1;

	private final RetrospectRepository retrospectRepository;
	private final AnalyzeRepository analyzeRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final MemberSpaceRelationRepository memberSpaceRelationRepository;

	private final OpenAIService openAIService;

	@Transactional
	@Async
	public void createAnalyze(Long spaceId, Long retrospectId, Long memberId, boolean isTeamAnalyze) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		// 회고 마감 여부 확인
		Retrospect retrospect = retrospectRepository.findByIdOrThrow(retrospectId);
		retrospect.validateRetrospectStatusDone();

		// 답변 조회
		Questions questions = new Questions(questionRepository.findAllByRetrospectIdOrderByQuestionOrder(retrospectId));
		Answers answers;
		AnalyzeType analyzeType;

		if(isTeamAnalyze){
			answers = new Answers(answerRepository.findAllByRetrospectIdAndAnswerStatus(retrospectId, AnswerStatus.DONE));
			analyzeType = AnalyzeType.TEAM;
		}
		else{
			answers = new Answers(answerRepository.findAllByRetrospectIdAndMemberIdAndAnswerStatus(retrospectId, memberId, AnswerStatus.DONE));
			analyzeType = AnalyzeType.INDIVIDUAL;
		}

		String totalAnswer = answers.getTotalAnswer(
			questions.extractEssentialQuestionIdBy(QuestionType.RANGER),
			questions.extractEssentialQuestionIdBy(QuestionType.NUMBER));

		// 분석 요청
		OpenAIResponse aiResponse = openAIService.createAnalyze(totalAnswer);
		OpenAIResponse.Content content = aiResponse.parseContent();

		Long numberQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.NUMBER);
		Long rangeQuestionId = questions.extractEssentialQuestionIdBy(QuestionType.RANGER);

		List<AnalyzeDetail> analyzeDetails = createAnalyzeDetails(content);
		Analyze analyze = createAnalyze(retrospectId, memberId, answers.getSatisfactionCount(rangeQuestionId),
			answers.getNormalCount(rangeQuestionId), answers.getRegretCount(rangeQuestionId),
			answers.getGoalCompletionRate(numberQuestionId), analyzeType, analyzeDetails);

		analyzeRepository.save(analyze);
	}

	public AnalyzeGetResponse getAnalyze(Long spaceId, Long retrospectId, Long memberId, AnalyzeType analyzeType) {
		// 해당 스페이스 팀원인지 검증
		Team team = new Team(memberSpaceRelationRepository.findAllBySpaceId(spaceId));
		team.validateTeamMembership(memberId);

		Analyze analyze = analyzeRepository.findByRetrospectIdAndAnalyzeTypeOrThrow(retrospectId, analyzeType);

		return AnalyzeGetResponse.of(analyze);
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

		for(OpenAIResponse.ContentDetail detail : contentDetails){
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

	private Analyze createAnalyze(Long retrospectId, Long memberId, int satisfactionCount, int normalCount, int regretCount,
		int goalCompletionRate, AnalyzeType analyzeType, List<AnalyzeDetail> analyzeDetails) {

		return Analyze.builder()
			.retrospectId(retrospectId)
			.memberId(memberId)
			.satisfactionCount(satisfactionCount)
			.normalCount(normalCount)
			.regretCount(regretCount)
			.goalCompletionRate(goalCompletionRate)
			.analyzeType(analyzeType)
			.analyzeDetails(analyzeDetails)
			.build();
	}

}
