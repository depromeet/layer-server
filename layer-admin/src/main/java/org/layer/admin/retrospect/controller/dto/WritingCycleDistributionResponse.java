package org.layer.admin.retrospect.controller.dto;

import java.util.List;

public record WritingCycleDistributionResponse(
    double averageIntervalDays,
    List<WritingCycleEntry> distribution
) {
}
