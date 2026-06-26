package com.edifika.communication.communication.interfaces.rest;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.domain.model.entities.AnnouncementRead;
import com.edifika.communication.communication.domain.services.CommunicationCommandService;
import com.edifika.communication.communication.domain.services.CommunicationQueryService;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementMetricsResource;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementResource;
import com.edifika.communication.communication.interfaces.rest.resources.CreateAnnouncementResource;
import com.edifika.communication.communication.interfaces.rest.resources.MarkAnnouncementAsReadResource;
import com.edifika.communication.communication.interfaces.rest.transform.AnnouncementResourceFromEntityAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationControllerTest {

    @Mock
    private CommunicationCommandService commandService;

    @Mock
    private CommunicationQueryService queryService;

    @InjectMocks
    private CommunicationController communicationController;

    @Test
    void shouldCreateAnnouncement() {

        CreateAnnouncementResource resource = mock(CreateAnnouncementResource.class);
        Announcement announcement = mock(Announcement.class);
        AnnouncementResource announcementResource = mock(AnnouncementResource.class);

        when(commandService.createAnnouncement(resource))
                .thenReturn(announcement);

        try (MockedStatic<AnnouncementResourceFromEntityAssembler> mockedAssembler =
                     mockStatic(AnnouncementResourceFromEntityAssembler.class)) {

            mockedAssembler
                    .when(() -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement))
                    .thenReturn(announcementResource);

            ResponseEntity<AnnouncementResource> response =
                    communicationController.createAnnouncement(resource);

            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertSame(announcementResource, response.getBody());

            verify(commandService, times(1))
                    .createAnnouncement(resource);

            verifyNoInteractions(queryService);

            mockedAssembler.verify(
                    () -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement),
                    times(1)
            );
        }
    }

    @Test
    void shouldGetAnnouncementsByBuilding() {

        Long buildingId = 1L;

        Announcement announcement1 = mock(Announcement.class);
        Announcement announcement2 = mock(Announcement.class);

        AnnouncementResource resource1 = mock(AnnouncementResource.class);
        AnnouncementResource resource2 = mock(AnnouncementResource.class);

        when(queryService.getAnnouncementsByBuildingId(buildingId))
                .thenReturn(List.of(announcement1, announcement2));

        try (MockedStatic<AnnouncementResourceFromEntityAssembler> mockedAssembler =
                     mockStatic(AnnouncementResourceFromEntityAssembler.class)) {

            mockedAssembler
                    .when(() -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement1))
                    .thenReturn(resource1);

            mockedAssembler
                    .when(() -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement2))
                    .thenReturn(resource2);

            ResponseEntity<?> response =
                    communicationController.getAnnouncementsByBuilding(buildingId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            List<?> body = (List<?>) response.getBody();

            assertEquals(2, body.size());
            assertSame(resource1, body.get(0));
            assertSame(resource2, body.get(1));

            verify(queryService, times(1))
                    .getAnnouncementsByBuildingId(buildingId);

            verifyNoInteractions(commandService);

            mockedAssembler.verify(
                    () -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement1),
                    times(1)
            );

            mockedAssembler.verify(
                    () -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement2),
                    times(1)
            );
        }
    }

    @Test
    void shouldGetAnnouncementById() {

        Long announcementId = 1L;

        Announcement announcement = mock(Announcement.class);
        AnnouncementResource announcementResource = mock(AnnouncementResource.class);

        when(queryService.getAnnouncementById(announcementId))
                .thenReturn(announcement);

        try (MockedStatic<AnnouncementResourceFromEntityAssembler> mockedAssembler =
                     mockStatic(AnnouncementResourceFromEntityAssembler.class)) {

            mockedAssembler
                    .when(() -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement))
                    .thenReturn(announcementResource);

            ResponseEntity<AnnouncementResource> response =
                    communicationController.getAnnouncementById(announcementId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertSame(announcementResource, response.getBody());

            verify(queryService, times(1))
                    .getAnnouncementById(announcementId);

            verifyNoInteractions(commandService);

            mockedAssembler.verify(
                    () -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement),
                    times(1)
            );
        }
    }

    @Test
    void shouldMarkAsRead() {

        Long announcementId = 1L;

        MarkAnnouncementAsReadResource resource =
                mock(MarkAnnouncementAsReadResource.class);

        AnnouncementRead announcementRead =
                mock(AnnouncementRead.class);

        when(commandService.markAnnouncementAsRead(announcementId, resource))
                .thenReturn(announcementRead);

        ResponseEntity<?> response =
                communicationController.markAsRead(announcementId, resource);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(commandService, times(1))
                .markAnnouncementAsRead(announcementId, resource);

        verifyNoInteractions(queryService);
    }

    @Test
    void shouldArchiveAnnouncement() {

        Long announcementId = 1L;

        Announcement announcement = mock(Announcement.class);
        AnnouncementResource announcementResource = mock(AnnouncementResource.class);

        when(commandService.archiveAnnouncement(announcementId))
                .thenReturn(announcement);

        try (MockedStatic<AnnouncementResourceFromEntityAssembler> mockedAssembler =
                     mockStatic(AnnouncementResourceFromEntityAssembler.class)) {

            mockedAssembler
                    .when(() -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement))
                    .thenReturn(announcementResource);

            ResponseEntity<AnnouncementResource> response =
                    communicationController.archiveAnnouncement(announcementId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertSame(announcementResource, response.getBody());

            verify(commandService, times(1))
                    .archiveAnnouncement(announcementId);

            verifyNoInteractions(queryService);

            mockedAssembler.verify(
                    () -> AnnouncementResourceFromEntityAssembler.toResourceFromEntity(announcement),
                    times(1)
            );
        }
    }

    @Test
    void shouldGetMetrics() {

        Long announcementId = 1L;

        AnnouncementMetricsResource metricsResource =
                mock(AnnouncementMetricsResource.class);

        when(queryService.getAnnouncementMetrics(announcementId))
                .thenReturn(metricsResource);

        ResponseEntity<AnnouncementMetricsResource> response =
                communicationController.getMetrics(announcementId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(metricsResource, response.getBody());

        verify(queryService, times(1))
                .getAnnouncementMetrics(announcementId);

        verifyNoInteractions(commandService);
    }
}