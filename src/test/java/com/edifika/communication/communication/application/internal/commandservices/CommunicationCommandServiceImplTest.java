package com.edifika.communication.communication.application.internal.commandservices;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.domain.model.entities.AnnouncementRead;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementReadRepository;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementRepository;
import com.edifika.communication.communication.interfaces.rest.resources.CreateAnnouncementResource;
import com.edifika.communication.communication.interfaces.rest.resources.MarkAnnouncementAsReadResource;
import com.edifika.communication.communication.interfaces.rest.transform.CreateAnnouncementFromResourceAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationCommandServiceImplTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private AnnouncementReadRepository announcementReadRepository;

    @InjectMocks
    private CommunicationCommandServiceImpl communicationCommandService;

    @Test
    void shouldCreateAnnouncement() {

        CreateAnnouncementResource resource = mock(CreateAnnouncementResource.class);
        Announcement announcement = mock(Announcement.class);

        try (MockedStatic<CreateAnnouncementFromResourceAssembler> mockedAssembler =
                     mockStatic(CreateAnnouncementFromResourceAssembler.class)) {

            mockedAssembler
                    .when(() -> CreateAnnouncementFromResourceAssembler.toEntityFromResource(resource))
                    .thenReturn(announcement);

            when(announcementRepository.save(announcement))
                    .thenReturn(announcement);

            Announcement result = communicationCommandService.createAnnouncement(resource);

            assertNotNull(result);
            assertSame(announcement, result);

            mockedAssembler.verify(
                    () -> CreateAnnouncementFromResourceAssembler.toEntityFromResource(resource),
                    times(1)
            );

            verify(announcementRepository, times(1))
                    .save(announcement);

            verifyNoInteractions(announcementReadRepository);
        }
    }

    @Test
    void shouldMarkAnnouncementAsRead() {

        Long announcementId = 1L;
        Long userId = 10L;

        MarkAnnouncementAsReadResource resource = mock(MarkAnnouncementAsReadResource.class);
        Announcement announcement = mock(Announcement.class);

        when(resource.userId()).thenReturn(userId);

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));

        when(announcementReadRepository.existsByAnnouncementIdAndUserId(announcementId, userId))
                .thenReturn(false);

        when(announcementReadRepository.save(any(AnnouncementRead.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AnnouncementRead result = communicationCommandService.markAnnouncementAsRead(
                announcementId,
                resource
        );

        assertNotNull(result);

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verify(announcementReadRepository, times(1))
                .existsByAnnouncementIdAndUserId(announcementId, userId);

        verify(announcementReadRepository, times(1))
                .save(any(AnnouncementRead.class));
    }



    @Test
    void shouldThrowExceptionWhenUserAlreadyReadAnnouncement() {

        Long announcementId = 1L;
        Long userId = 10L;

        MarkAnnouncementAsReadResource resource = mock(MarkAnnouncementAsReadResource.class);
        Announcement announcement = mock(Announcement.class);

        when(resource.userId()).thenReturn(userId);

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));

        when(announcementReadRepository.existsByAnnouncementIdAndUserId(announcementId, userId))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> communicationCommandService.markAnnouncementAsRead(announcementId, resource)
        );

        assertEquals("User already read this announcement", exception.getMessage());

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verify(announcementReadRepository, times(1))
                .existsByAnnouncementIdAndUserId(announcementId, userId);

        verify(announcementReadRepository, never())
                .save(any(AnnouncementRead.class));
    }

    @Test
    void shouldArchiveAnnouncement() {

        Long announcementId = 1L;
        Announcement announcement = mock(Announcement.class);

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));

        when(announcementRepository.save(announcement))
                .thenReturn(announcement);

        Announcement result = communicationCommandService.archiveAnnouncement(announcementId);

        assertNotNull(result);
        assertSame(announcement, result);

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verify(announcement, times(1))
                .archive();

        verify(announcementRepository, times(1))
                .save(announcement);

        verifyNoInteractions(announcementReadRepository);
    }

    @Test
    void shouldThrowExceptionWhenAnnouncementDoesNotExistWhileArchiving() {

        Long announcementId = 1L;

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> communicationCommandService.archiveAnnouncement(announcementId)
        );

        assertEquals("Announcement not found", exception.getMessage());

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verify(announcementRepository, never())
                .save(any(Announcement.class));

        verifyNoInteractions(announcementReadRepository);
    }
}