package org.layer.admin.retrospect.controller.dto;

import java.util.List;

public record CompletionTrendResponse(List<MonthlyCompletionRate> months) {
}
