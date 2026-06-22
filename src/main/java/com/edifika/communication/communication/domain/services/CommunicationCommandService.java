package com.edifika.communication.communication.domain.services;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.domain.model.entities.AnnouncementRead;
import com.edifika.communication.communication.interfaces.rest.resources.CreateAnnouncementResource;
import com.edifika.communication.communication.interfaces.rest.resources.MarkAnnouncementAsReadResource;

public interface CommunicationCommandService {

    Announcement createAnnouncement(CreateAnnouncementResource resource);

    AnnouncementRead markAnnouncementAsRead(
            Long announcementId,
            MarkAnnouncementAsReadResource resource
    );

    Announcement archiveAnnouncement(Long announcementId);
}