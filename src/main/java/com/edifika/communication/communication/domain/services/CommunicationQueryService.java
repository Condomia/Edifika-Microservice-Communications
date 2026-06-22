package com.edifika.communication.communication.domain.services;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementMetricsResource;

import java.util.List;

public interface CommunicationQueryService {

    List<Announcement> getAnnouncementsByBuildingId(Long buildingId);

    Announcement getAnnouncementById(Long announcementId);

    AnnouncementMetricsResource getAnnouncementMetrics(Long announcementId);
}