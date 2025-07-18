package org.layer.admin.template.listener;

import org.layer.admin.common.UserOnlyEventListener;
import org.layer.admin.template.service.AdminTemplateService;
import org.layer.event.template.TemplateListViewEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateListViewEventListener {
	private final AdminTemplateService adminTemplateService;

	@UserOnlyEventListener
	@EventListener
	public void handleTemplateListView(TemplateListViewEvent event) {
		adminTemplateService.saveTemplateListViewHistory(event);
	}
}
