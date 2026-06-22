package com.edifika.communication.communication.application.internal.outbounds;

public record AnnouncementMetricsResponse(
        Long announcementId,
        Long totalResidents,
        Long readResidents,
        Double reachPercentage
) {
}