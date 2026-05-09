package org.layer.admin.retrospect.controller.dto;

public record MonthlyCompletionRate(
    String month,
    double overallRate,
    double teamRate,
    double individualRate
) {
}
