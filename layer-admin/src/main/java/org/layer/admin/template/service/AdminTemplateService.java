package org.layer.admin.template.service;

import static org.springframework.transaction.annotation.Propagation.*;

import java.util.List;

import org.layer.admin.template.controller.dto.TemplateRecommendedCountResponse;
import org.layer.admin.template.entity.AdminTemplateRecommendation;
import org.layer.admin.template.enums.AdminFormTag;
import org.layer.admin.template.repository.AdminTemplateRepository;
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
	private final AdminTemplateRepository adminTemplateRepository;

	public List<TemplateRecommendedCountResponse> getTemplateRecommendedCount(){
		return adminTemplateRepository.countByFormTag();
	}

	@Transactional(propagation = REQUIRES_NEW)
	@Async
	public void saveTemplateRecommendation(TemplateRecommendedEvent event) {
		AdminTemplateRecommendation recommendation = AdminTemplateRecommendation.builder()
			.formTag(AdminFormTag.from(event.formTag()))
			.eventTime(event.eventTime())
			.memberId(event.memberId())
			.eventId(event.eventId())
			.build();

		adminTemplateRepository.save(recommendation);
	}
}
