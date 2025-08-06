package org.layer.admin.template.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.template.service.AdminTemplateService;
import org.layer.event.template.TemplateRecommendedChoiceEvent;
import org.layer.event.template.TemplateRecommendedClickEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateRecommendedEventListener {
	private final AdminTemplateService adminTemplateService;

	@UserOnlyEventListener
	@EventListener
	public void handleTemplateRecommendedChoiceEvent(TemplateRecommendedChoiceEvent event) {
		adminTemplateService.saveTemplateChoice(event);
	}

	@UserOnlyEventListener
	@EventListener
	public void handleTemplateRecommendedClickEvent(TemplateRecommendedClickEvent event) {
		adminTemplateService.saveTemplateClickHistory(event);
	}
}
