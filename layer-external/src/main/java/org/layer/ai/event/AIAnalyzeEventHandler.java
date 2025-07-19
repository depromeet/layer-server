package org.layer.ai.event;

import org.layer.ai.service.AIAnalyzeService;
import org.layer.event.ai.AIAnalyzeStartEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AIAnalyzeEventHandler {
	private final AIAnalyzeService aiAnalyzeService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleRetrospectCompleted(AIAnalyzeStartEvent event) {
		aiAnalyzeService.createAnalyze(event.retrospectId());
	}
}
