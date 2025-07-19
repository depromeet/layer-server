package org.layer.listener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.layer.ai.event.AIAnalyzeStartEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestAIAnalyzeEventListener {

	private CountDownLatch latch = new CountDownLatch(1);
	private Long receivedRetrospectId;

	@EventListener
	public void onEvent(AIAnalyzeStartEvent event) {
		receivedRetrospectId = event.retrospectId();
		latch.countDown();
	}

	public void reset() {
		latch = new CountDownLatch(1);
		receivedRetrospectId = null;
	}

	public boolean await() throws InterruptedException {
		return latch.await(5, TimeUnit.SECONDS);
	}

	public Long getReceivedRetrospectId() {
		return receivedRetrospectId;
	}
}

