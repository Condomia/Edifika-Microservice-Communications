package com.edifika.communication.communication.interfaces.rest.transform;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.interfaces.rest.resources.CreateAnnouncementResource;

public class CreateAnnouncementFromResourceAssembler {

    public static Announcement toEntityFromResource(CreateAnnouncementResource resource) {
        return new Announcement(
                resource.buildingId(),
                resource.createdByUserId(),
                resource.title(),
                resource.description(),
                resource.message(),
                resource.priority()
        );
    }
}