package org.layer.admin.template.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateChoiceCountResponse;
import org.layer.admin.template.controller.dto.TemplateViewCountResponse;
import org.layer.admin.template.entity.AdminTemplateChoice;
import org.layer.admin.template.entity.AdminTemplateViewHistory;
import org.layer.admin.template.enums.AdminFormTag;
import org.layer.admin.template.enums.AdminChoiceType;
import org.layer.admin.template.repository.AdminTemplateRecommendationRepository;
import org.layer.admin.template.repository.AdminTemplateViewHistoryRepository;
import org.layer.event.template.TemplateListViewEvent;
import org.layer.event.template.TemplateRecommendedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTemplateService {
	private final AdminTemplateRecommendationRepository templateRecommendationRepository;
	private final AdminTemplateViewHistoryRepository templateViewHistoryRepository;

	public List<TemplateChoiceCountResponse> getTemplateChoiceCount(
		LocalDateTime startDate, LocalDateTime endDate, AdminChoiceType choiceType) {

		if( choiceType != null) {
			return templateRecommendationRepository.countByChoiceType(startDate, endDate, choiceType.getType());
		}

		return templateRecommendationRepository.countAll(startDate, endDate);
	}

	public List<TemplateViewCountResponse> getTemplateRecommendedListCount(
		LocalDateTime startDate, LocalDateTime endDate) {

		return templateViewHistoryRepository.countByViewType(startDate, endDate);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateRecommendation(TemplateRecommendedEvent event) {
		AdminTemplateChoice recommendation = AdminTemplateChoice.builder()
			.formTag(AdminFormTag.from(event.formTag()))
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		templateRecommendationRepository.save(recommendation);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateRecommendedViewHistory(TemplateRecommendedEvent event) {
		AdminTemplateViewHistory viewHistory = AdminTemplateViewHistory.builder()
			.viewType(AdminChoiceType.RECOMMENDATION)
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		templateViewHistoryRepository.save(viewHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateListViewHistory(TemplateListViewEvent event) {
		AdminTemplateViewHistory viewHistory = AdminTemplateViewHistory.builder()
			.viewType(AdminChoiceType.LIST_VIEW)
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		templateViewHistoryRepository.save(viewHistory);
	}
}
