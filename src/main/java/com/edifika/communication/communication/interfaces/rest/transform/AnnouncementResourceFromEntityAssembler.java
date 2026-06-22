package com.edifika.communication.communication.interfaces.rest.transform;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementResource;

public class AnnouncementResourceFromEntityAssembler {

    public static AnnouncementResource toResourceFromEntity(Announcement entity) {
        return new AnnouncementResource(
                entity.getId(),
                entity.getBuildingId(),
                entity.getCreatedByUserId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getMessage(),
                entity.getPriority(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}