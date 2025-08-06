package org.layer.admin.template.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.layer.admin.template.controller.dto.TemplateChoiceCountResponse;
import org.layer.admin.template.controller.dto.TemplateClickCountResponse;
import org.layer.admin.template.entity.AdminTemplateChoice;
import org.layer.admin.template.entity.AdminTemplateClickHistory;
import org.layer.admin.template.enums.AdminFormTag;
import org.layer.admin.template.enums.AdminChoiceType;
import org.layer.admin.template.repository.AdminTemplateChoiceRepository;
import org.layer.admin.template.repository.AdminTemplateClickHistoryRepository;
import org.layer.event.template.TemplateListViewChoiceEvent;
import org.layer.event.template.TemplateListViewClickEvent;
import org.layer.event.template.TemplateRecommendedChoiceEvent;
import org.layer.event.template.TemplateRecommendedClickEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTemplateService {
	private final AdminTemplateChoiceRepository templateChoiceRepository;
	private final AdminTemplateClickHistoryRepository templateClickHistoryRepository;

	public List<TemplateChoiceCountResponse> getTemplateChoiceCount(
		LocalDateTime startDate, LocalDateTime endDate, AdminChoiceType choiceType) {

		if (choiceType != null) {
			return templateChoiceRepository.countByChoiceType(startDate, endDate, choiceType);
		}

		return templateChoiceRepository.countAll(startDate, endDate);
	}

	public List<TemplateClickCountResponse> getTemplateClickCount(
		LocalDateTime startDate, LocalDateTime endDate) {

		return templateClickHistoryRepository.countByViewType(startDate, endDate);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateChoice(TemplateRecommendedChoiceEvent event) {
		AdminTemplateChoice choice = AdminTemplateChoice.builder()
			.formTag(AdminFormTag.from(event.formTag()))
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.choiceType(AdminChoiceType.RECOMMENDATION)
			.build();

		templateChoiceRepository.save(choice);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateChoice(TemplateListViewChoiceEvent event) {
		AdminTemplateChoice choice = AdminTemplateChoice.builder()
			.formTag(AdminFormTag.from(event.formTag()))
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.choiceType(AdminChoiceType.LIST_VIEW)
			.build();

		templateChoiceRepository.save(choice);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateClickHistory(TemplateRecommendedClickEvent event) {
		AdminTemplateClickHistory clickHistory = AdminTemplateClickHistory.builder()
			.viewType(AdminChoiceType.RECOMMENDATION)
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		templateClickHistoryRepository.save(clickHistory);
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateClickHistory(TemplateListViewClickEvent event) {
		AdminTemplateClickHistory clickHistory = AdminTemplateClickHistory.builder()
			.viewType(AdminChoiceType.LIST_VIEW)
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		templateClickHistoryRepository.save(clickHistory);
	}
}
