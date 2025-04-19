package org.layer.domain.answer.entity;

import static org.layer.global.exception.AnswerExceptionType.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.layer.domain.answer.enums.AnswerStatus;
import org.layer.domain.answer.exception.AnswerException;
import org.layer.domain.retrospect.entity.WriteStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Answers {
	private static final int ZERO = 0;
	public static final int SCORE_ONE = 0;
	public static final int SCORE_TWO = 1;
	public static final int SCORE_THREE = 2;
	public static final int SCORE_FOUR = 3;
	public static final int SCORE_FIVE = 4;

	private final List<Answer> answers;

	public String getAnswerToQuestion(Long questionId, Long memberId) {
		return answers.stream()
			.filter(answer -> answer.getQuestionId().equals(questionId) && answer.getMemberId().equals(memberId))
			.map(Answer::getContent)
			.findFirst()
			.orElse(null);
	}

	public boolean hasRetrospectAnswer(Long memberId, Long retrospectId) {
		return answers.stream()
			.filter(answer -> answer.getRetrospectId().equals(retrospectId))
			.anyMatch(answer -> answer.getMemberId().equals(memberId));
	}

	public WriteStatus getWriteStatus(Long memberId, Long retrospectId) {
		boolean isDoneWrite = answers.stream()
			.filter(answer -> answer.getRetrospectId().equals(retrospectId))
			.filter(answer -> answer.getMemberId().equals(memberId))
			.anyMatch(answer -> answer.getAnswerStatus().equals(AnswerStatus.DONE));
		if (isDoneWrite) {
			return WriteStatus.DONE;
		}

		boolean isTemporaryWrite = answers.stream()
			.filter(answer -> answer.getRetrospectId().equals(retrospectId))
			.filter(answer -> answer.getMemberId().equals(memberId))
			.anyMatch(answer -> answer.getAnswerStatus().equals(AnswerStatus.TEMPORARY));
		if (isTemporaryWrite) {
			return WriteStatus.PROCEEDING;
		}

		return WriteStatus.NOT_STARTED;
	}

	public long getWriteCount(Long retrospectId) {

		Map<Long, List<Answer>> answersByRetrospectId = answers.stream()
			.collect(Collectors.groupingBy(Answer::getRetrospectId));

		Set<Long> answerMembers = new HashSet<>();

		if (!answersByRetrospectId.containsKey(retrospectId)) {
			return 0L;
		}

		answersByRetrospectId.get(retrospectId).stream()
			.filter(answer -> answer.getAnswerStatus().equals(AnswerStatus.DONE))
			.forEach(answer -> answerMembers.add(answer.getMemberId()));

		return answerMembers.size();
	}

	public List<Long> getWriteMemberIds() {
		Set<Long> set = new HashSet<>();

		answers.forEach(answer -> {
			// 임시저장된 회고일 경우 제외
			if (answer.getAnswerStatus() != AnswerStatus.TEMPORARY) {
				set.add(answer.getMemberId());
			}
		});

		return new ArrayList<>(set);
	}

	public void validateNoAnswer() {
		if (answers.size() != ZERO) {
			throw new AnswerException(ALREADY_ANSWERED);
		}
	}

	public void validateContainAnswers() {
		if (answers.size() == ZERO)
			throw new AnswerException(NOT_CONTAIN_ANSWERS);
	}

	public void validateAlreadyAnswer(Long memberId, Long retrospectId) {
		if (!hasRetrospectAnswer(memberId, retrospectId)) {
			throw new AnswerException(NOT_ANSWERED);
		}
	}

	public String getTotalAnswer(Long rangeQuestionId, Long numberQuestionId) {
		Map<Long, String> answerConcatMap = answers.stream()
			.filter(answer -> !answer.getQuestionId().equals(rangeQuestionId) && !answer.getQuestionId()
				.equals(numberQuestionId))
			.collect(Collectors.groupingBy(
				Answer::getMemberId, Collectors.mapping(Answer::getContent, Collectors.joining(" "))));

		Map<Long, String> answerMap = answerConcatMap.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey, entry -> entry.getKey() + " 번 사용자: " + entry.getValue()
			));

		return String.join("\n&&\n", answerMap.values());
	}

	public String getIndividualAnswer(Long rangeQuestionId, Long numberQuestionId, Long memberId) {
		Map<Long, String> answerConcatMap = answers.stream()
			.filter(answer -> answer.getMemberId().equals(memberId))
			.filter(answer -> !answer.getQuestionId().equals(rangeQuestionId) && !answer.getQuestionId()
				.equals(numberQuestionId))
			.collect(Collectors.groupingBy(
				Answer::getMemberId, Collectors.mapping(Answer::getContent, Collectors.joining(" "))));

		Map<Long, String> answerMap = answerConcatMap.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey, entry -> entry.getKey() + " 번 사용자: " + entry.getValue()
			));

		return String.join("\n&&\n", answerMap.values());
	}

	public int getGoalCompletionRate(Long questionId) {

		int sum = 0;
		int count = 0;
		for (Answer answer : answers) {
			if (answer.getQuestionId().equals(questionId)) {
				sum += Integer.parseInt(answer.getContent());
				count++;
			}
		}

		if (count == ZERO) {
			throw new AnswerException(NOT_CONTAIN_ANSWERS);
		}

		return sum / count;
	}

	public int getScoreCount(Long questionId, int score, Long memberId) {
		return (int)answers.stream()
			.filter(answer -> answer.getQuestionId().equals(questionId))
			.filter(answer -> Integer.parseInt(answer.getContent()) == score)
			.filter(answer -> memberId == null || answer.getMemberId().equals(memberId))
			.count();
	}

}
