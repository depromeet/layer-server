package org.layer.domain.answer.entity;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Answers {
	private final List<Answer> answers;

	public boolean hasRetrospectAnswer(Long memberId) {
		return answers.stream()
			.anyMatch(answer -> answer.getMemberId().equals(memberId));
	}

	public int getWriteCount() {
		return answers.size();
	}
}
