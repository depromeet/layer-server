package org.layer.admin.retrospect.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.layer.admin.retrospect.controller.dto.RetrospectStayTimeResponse;
import org.layer.admin.retrospect.entity.AdminRetrospectAnswerHistory;
import org.layer.admin.retrospect.enums.AnswerTimeRange;
import org.layer.admin.retrospect.repository.AdminRetrospectRepository;
import org.layer.event.retrospect.WriteRetrospectEndEvent;
import org.layer.event.retrospect.WriteRetrospectStartEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminRetrospectService {

	private final AdminRetrospectRepository adminRetrospectRepository;

	public List<RetrospectStayTimeResponse> getAllRetrospectStayTime(
		LocalDateTime startTime, LocalDateTime endTime) {
		List<AdminRetrospectAnswerHistory> retrospectAnswerHistories = adminRetrospectRepository.findAllByEventTimeBetween(
			startTime, endTime);

		Map<AnswerTimeRange, Long> countMap = new HashMap<>();
		retrospectAnswerHistories.forEach(history -> {
			AnswerTimeRange range = AnswerTimeRange.from(history.getAnswerTime());
			countMap.put(range, countMap.getOrDefault(range, 0L) + 1);
		});

		List<RetrospectStayTimeResponse> response = new ArrayList<>();
		for (AnswerTimeRange range : AnswerTimeRange.values()) {
			Long count = countMap.getOrDefault(range, 0L);
			response.add(new RetrospectStayTimeResponse(range.getLabel(), count));
		}

		return response;
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveRetrospectAnswerHistory(WriteRetrospectStartEvent event) {
		adminRetrospectRepository.deleteByMemberIdAndSpaceIdAndRetrospectId(event.memberId(), event.spaceId(),
			event.retrospectId());

		AdminRetrospectAnswerHistory retrospectAnswerHistory = AdminRetrospectAnswerHistory.builder()
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.spaceId(event.spaceId())
			.retrospectId(event.retrospectId())
			.answerStartTime(event.eventTime())
			.build();

		adminRetrospectRepository.save(retrospectAnswerHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void updateRetrospectAnswerHistory(WriteRetrospectEndEvent event) {

		adminRetrospectRepository.findTopByMemberIdAndSpaceIdAndRetrospectIdOrderByAnswerStartTimeDesc(
			event.memberId(), event.spaceId(), event.retrospectId())
			.ifPresentOrElse(
				history -> {
					history.updateRetrospectCompleted(event.eventTime(), event.answerContent());
					adminRetrospectRepository.save(history);
				},
				() -> {
					AdminRetrospectAnswerHistory retrospectAnswerHistory = AdminRetrospectAnswerHistory.builder()
						.eventTime(event.eventTime())
						.memberId(event.memberId())
						.eventId(event.eventId())
						.spaceId(event.spaceId())
						.retrospectId(event.retrospectId())
						.answerEndTime(event.eventTime())
						.answerContent(event.answerContent())
						.build();
					adminRetrospectRepository.save(retrospectAnswerHistory);
				}
			);
	}
}
