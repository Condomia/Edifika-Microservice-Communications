package com.edifika.communication.communication.infrastructure.persistence.jpa.repositories;

import com.edifika.communication.communication.domain.model.aggregates.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByBuildingId(Long buildingId);
}