package org.layer.admin.template.listener;

import org.layer.admin.template.service.AdminTemplateService;
import org.layer.event.template.TemplateRecommendedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateRecommendedEventListener {
	private final AdminTemplateService adminTemplateService;

	@EventListener
	public void handleTemplateRecommended(TemplateRecommendedEvent event) {
		adminTemplateService.saveTemplateRecommendation(event);
	}
}
