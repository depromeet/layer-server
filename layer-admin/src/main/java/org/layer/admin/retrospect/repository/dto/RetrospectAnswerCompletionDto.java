package org.layer.admin.retrospect.repository.dto;

public record RetrospectAnswerCompletionDto(
	Long retrospectId,
	Long targetAnswerCount,
	Long actualAnswerCount
) {
	public RetrospectAnswerCompletionDto(Long retrospectId, Long targetAnswerCount, Long actualAnswerCount) {
		this.retrospectId = retrospectId;
		this.targetAnswerCount = targetAnswerCount;
		this.actualAnswerCount = actualAnswerCount;
	}
}

