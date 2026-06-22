package com.edifika.communication.communication.interfaces.rest.resources;

import com.edifika.communication.communication.domain.model.valueobjects.AnnouncementPriority;
import com.edifika.communication.communication.domain.model.valueobjects.AnnouncementStatus;

import java.util.Date;

public record AnnouncementResource(
        Long id,
        Long buildingId,
        Long createdByUserId,
        String title,
        String description,
        String message,
        AnnouncementPriority priority,
        AnnouncementStatus status,
        Date createdAt
) {
}