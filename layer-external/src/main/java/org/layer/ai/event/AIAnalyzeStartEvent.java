package org.layer.ai.event;

public record AIAnalyzeStartEvent(Long retrospectId) {
	public static AIAnalyzeStartEvent of(Long retrospectId) {
		return new AIAnalyzeStartEvent(retrospectId);
	}
}
