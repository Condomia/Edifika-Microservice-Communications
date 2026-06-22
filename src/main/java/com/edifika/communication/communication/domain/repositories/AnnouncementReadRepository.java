package com.edifika.communication.communication.domain.repositories;

import com.edifika.communication.communication.domain.model.entities.AnnouncementRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementReadRepository extends JpaRepository<AnnouncementRead, Long> {

    long countByAnnouncementId(Long announcementId);

    boolean existsByAnnouncementIdAndUserId(Long announcementId, Long userId);
}