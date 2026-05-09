package org.layer.admin.retrospect.controller.dto;

public record RetrospectFunnelResponse(
    long createdCount,
    long startedCount,
    double startedRate,
    long qualityCount,
    double qualityRate,
    long submittedCount,
    double submittedRate
) {
}
