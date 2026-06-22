package com.edifika.communication.communication.interfaces.rest.resources;

public record AnnouncementMetricsResource(
        Long announcementId,
        Long totalResidents,
        Long readResidents,
        Double reachPercentage
) {
}