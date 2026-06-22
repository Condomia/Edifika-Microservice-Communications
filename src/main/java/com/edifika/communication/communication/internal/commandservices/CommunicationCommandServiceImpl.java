package com.edifika.communication.communication.internal.commandservices;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.domain.model.entities.AnnouncementRead;
import com.edifika.communication.communication.domain.repositories.AnnouncementReadRepository;
import com.edifika.communication.communication.domain.repositories.AnnouncementRepository;
import com.edifika.communication.communication.domain.services.CommunicationCommandService;
import com.edifika.communication.communication.interfaces.rest.resources.CreateAnnouncementResource;
import com.edifika.communication.communication.interfaces.rest.resources.MarkAnnouncementAsReadResource;
import com.edifika.communication.communication.interfaces.rest.transform.CreateAnnouncementFromResourceAssembler;
import org.springframework.stereotype.Service;

@Service
public class CommunicationCommandServiceImpl implements CommunicationCommandService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementReadRepository announcementReadRepository;

    public CommunicationCommandServiceImpl(
            AnnouncementRepository announcementRepository,
            AnnouncementReadRepository announcementReadRepository
    ) {
        this.announcementRepository = announcementRepository;
        this.announcementReadRepository = announcementReadRepository;
    }

    @Override
    public Announcement createAnnouncement(CreateAnnouncementResource resource) {

        var announcement =
                CreateAnnouncementFromResourceAssembler
                        .toEntityFromResource(resource);

        return announcementRepository.save(announcement);
    }

    @Override
    public AnnouncementRead markAnnouncementAsRead(
            Long announcementId,
            MarkAnnouncementAsReadResource resource
    ) {

        announcementRepository.findById(announcementId)
                .orElseThrow(() ->
                        new RuntimeException("Announcement not found"));

        boolean alreadyRead =
                announcementReadRepository
                        .existsByAnnouncementIdAndUserId(
                                announcementId,
                                resource.userId()
                        );

        if (alreadyRead) {
            throw new RuntimeException(
                    "User already read this announcement");
        }

        var read = new AnnouncementRead(
                announcementId,
                resource.userId()
        );

        return announcementReadRepository.save(read);
    }

    @Override
    public Announcement archiveAnnouncement(Long announcementId) {

        var announcement =
                announcementRepository.findById(announcementId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Announcement not found"));

        announcement.archive();

        return announcementRepository.save(announcement);
    }
}