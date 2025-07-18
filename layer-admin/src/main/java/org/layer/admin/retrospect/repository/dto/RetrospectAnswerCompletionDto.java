package org.layer.admin.retrospect.repository.dto;

public record RetrospectAnswerCompletionDto(
	Long retrospectId,
	Long targetAnswerCount,
	Long actualAnswerCount
) {}

