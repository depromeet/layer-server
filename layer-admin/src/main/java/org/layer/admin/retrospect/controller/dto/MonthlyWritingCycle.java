package org.layer.admin.retrospect.controller.dto;

import java.util.List;

public record MonthlyWritingCycle(String month, List<WritingCycleEntry> distribution) {
}
