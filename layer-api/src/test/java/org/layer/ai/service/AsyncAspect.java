package org.layer.ai.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
class AsyncAspect {

	private static CountDownLatch latch;

	public void init() {
		latch = new CountDownLatch(1);
	}

	@After("execution(* org.layer.ai.service.AIAnalyzeService.createAnalyze(*))")
	public void asyncAfter() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					latch.countDown(); // 트랜잭션 커밋 후 실행
				}
			});
		} else {
			// 트랜잭션 없으면 즉시 실행
			latch.countDown();
		}
	}

	public void await() throws InterruptedException {
		latch.await();
	}
}
