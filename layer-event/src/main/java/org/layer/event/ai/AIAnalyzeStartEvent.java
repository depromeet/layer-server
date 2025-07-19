package org.layer.event.ai;

public record AIAnalyzeStartEvent(Long retrospectId) {
	public static AIAnalyzeStartEvent of(Long retrospectId) {
		return new AIAnalyzeStartEvent(retrospectId);
	}
}
