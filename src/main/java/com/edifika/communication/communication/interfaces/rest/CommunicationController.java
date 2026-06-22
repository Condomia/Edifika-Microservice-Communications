package com.edifika.communication.communication.interfaces.rest;

import com.edifika.communication.communication.domain.services.CommunicationCommandService;
import com.edifika.communication.communication.domain.services.CommunicationQueryService;
import com.edifika.communication.communication.interfaces.rest.resources.*;
import com.edifika.communication.communication.interfaces.rest.transform.AnnouncementResourceFromEntityAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/announcements")
public class CommunicationController {

    private final CommunicationCommandService commandService;
    private final CommunicationQueryService queryService;

    public CommunicationController(
            CommunicationCommandService commandService,
            CommunicationQueryService queryService
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<AnnouncementResource> createAnnouncement(
            @RequestBody CreateAnnouncementResource resource
    ) {

        var announcement =
                commandService.createAnnouncement(resource);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        AnnouncementResourceFromEntityAssembler
                                .toResourceFromEntity(announcement)
                );
    }

    @GetMapping
    public ResponseEntity<?> getAnnouncementsByBuilding(
            @RequestParam Long buildingId
    ) {

        var announcements =
                queryService.getAnnouncementsByBuildingId(buildingId)
                        .stream()
                        .map(
                                AnnouncementResourceFromEntityAssembler
                                        ::toResourceFromEntity
                        )
                        .collect(Collectors.toList());

        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResource> getAnnouncementById(
            @PathVariable Long announcementId
    ) {

        var announcement =
                queryService.getAnnouncementById(announcementId);

        return ResponseEntity.ok(
                AnnouncementResourceFromEntityAssembler
                        .toResourceFromEntity(announcement)
        );
    }

    @PostMapping("/{announcementId}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long announcementId,
            @RequestBody MarkAnnouncementAsReadResource resource
    ) {

        commandService.markAnnouncementAsRead(
                announcementId,
                resource
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{announcementId}/archive")
    public ResponseEntity<AnnouncementResource> archiveAnnouncement(
            @PathVariable Long announcementId
    ) {

        var announcement =
                commandService.archiveAnnouncement(announcementId);

        return ResponseEntity.ok(
                AnnouncementResourceFromEntityAssembler
                        .toResourceFromEntity(announcement)
        );
    }

    @GetMapping("/{announcementId}/metrics")
    public ResponseEntity<AnnouncementMetricsResource> getMetrics(
            @PathVariable Long announcementId
    ) {

        return ResponseEntity.ok(
                queryService.getAnnouncementMetrics(announcementId)
        );
    }
}