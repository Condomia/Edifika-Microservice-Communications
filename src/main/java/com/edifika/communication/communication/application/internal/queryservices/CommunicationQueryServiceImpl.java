package com.edifika.communication.communication.application.internal.queryservices;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementReadRepository;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementRepository;
import com.edifika.communication.communication.domain.services.CommunicationQueryService;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementMetricsResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationQueryServiceImpl
        implements CommunicationQueryService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementReadRepository announcementReadRepository;

    public CommunicationQueryServiceImpl(
            AnnouncementRepository announcementRepository,
            AnnouncementReadRepository announcementReadRepository
    ) {
        this.announcementRepository = announcementRepository;
        this.announcementReadRepository = announcementReadRepository;
    }

    @Override
    public List<Announcement> getAnnouncementsByBuildingId(Long buildingId) {
        return announcementRepository.findByBuildingId(buildingId);
    }

    @Override
    public Announcement getAnnouncementById(Long announcementId) {

        return announcementRepository.findById(announcementId)
                .orElseThrow(() ->
                        new RuntimeException("Announcement not found"));
    }

    @Override
    public AnnouncementMetricsResource getAnnouncementMetrics(
            Long announcementId
    ) {

        long readResidents =
                announcementReadRepository
                        .countByAnnouncementId(announcementId);

        long totalResidents = 100L;

        double reachPercentage =
                totalResidents == 0
                        ? 0
                        : ((double) readResidents / totalResidents) * 100;

        return new AnnouncementMetricsResource(
                announcementId,
                totalResidents,
                readResidents,
                reachPercentage
        );
    }
}