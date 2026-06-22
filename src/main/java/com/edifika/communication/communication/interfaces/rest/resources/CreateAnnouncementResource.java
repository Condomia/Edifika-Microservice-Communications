package com.edifika.communication.communication.interfaces.rest.resources;

import com.edifika.communication.communication.domain.model.valueobjects.AnnouncementPriority;

public record CreateAnnouncementResource(
        Long buildingId,
        Long createdByUserId,
        String title,
        String description,
        String message,
        AnnouncementPriority priority
) {
}