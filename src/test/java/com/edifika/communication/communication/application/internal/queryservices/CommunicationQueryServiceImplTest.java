package com.edifika.communication.communication.application.internal.queryservices;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementReadRepository;
import com.edifika.communication.communication.infrastructure.persistence.jpa.repositories.AnnouncementRepository;
import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementMetricsResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationQueryServiceImplTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private AnnouncementReadRepository announcementReadRepository;

    @InjectMocks
    private CommunicationQueryServiceImpl communicationQueryService;

    @Test
    void shouldGetAnnouncementsByBuildingId() {

        Long buildingId = 1L;

        Announcement announcement1 = mock(Announcement.class);
        Announcement announcement2 = mock(Announcement.class);

        List<Announcement> expectedAnnouncements = List.of(
                announcement1,
                announcement2
        );

        when(announcementRepository.findByBuildingId(buildingId))
                .thenReturn(expectedAnnouncements);

        List<Announcement> result =
                communicationQueryService.getAnnouncementsByBuildingId(buildingId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(expectedAnnouncements, result);

        verify(announcementRepository, times(1))
                .findByBuildingId(buildingId);

        verifyNoInteractions(announcementReadRepository);
    }

    @Test
    void shouldGetAnnouncementById() {

        Long announcementId = 1L;

        Announcement announcement = mock(Announcement.class);

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.of(announcement));

        Announcement result =
                communicationQueryService.getAnnouncementById(announcementId);

        assertNotNull(result);
        assertSame(announcement, result);

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verifyNoInteractions(announcementReadRepository);
    }

    @Test
    void shouldThrowExceptionWhenAnnouncementDoesNotExist() {

        Long announcementId = 1L;

        when(announcementRepository.findById(announcementId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> communicationQueryService.getAnnouncementById(announcementId)
        );

        assertEquals("Announcement not found", exception.getMessage());

        verify(announcementRepository, times(1))
                .findById(announcementId);

        verifyNoInteractions(announcementReadRepository);
    }

    @Test
    void shouldGetAnnouncementMetrics() {

        Long announcementId = 1L;
        long readResidents = 35L;

        when(announcementReadRepository.countByAnnouncementId(announcementId))
                .thenReturn(readResidents);

        AnnouncementMetricsResource result =
                communicationQueryService.getAnnouncementMetrics(announcementId);

        assertNotNull(result);

        assertEquals(announcementId, result.announcementId());
        assertEquals(100L, result.totalResidents());
        assertEquals(readResidents, result.readResidents());
        assertEquals(35.0, result.reachPercentage());

        verify(announcementReadRepository, times(1))
                .countByAnnouncementId(announcementId);

        verifyNoInteractions(announcementRepository);
    }
}