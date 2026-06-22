package com.edifika.communication.communication.domain.model.aggregates;

import com.edifika.communication.communication.domain.model.valueobjects.AnnouncementPriority;
import com.edifika.communication.communication.domain.model.valueobjects.AnnouncementStatus;
import com.edifika.communication.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "announcements")
public class Announcement extends AuditableAbstractAggregateRoot<Announcement> {

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    private Long createdByUserId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, length = 5000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnouncementPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnouncementStatus status;

    protected Announcement() {
    }

    public Announcement(
            Long buildingId,
            Long createdByUserId,
            String title,
            String description,
            String message,
            AnnouncementPriority priority) {

        this.buildingId = buildingId;
        this.createdByUserId = createdByUserId;
        this.title = title;
        this.description = description;
        this.message = message;
        this.priority = priority;
        this.status = AnnouncementStatus.PUBLISHED;
    }



    public void publish() {
        this.status = AnnouncementStatus.PUBLISHED;
    }

    public void archive() {
        this.status = AnnouncementStatus.ARCHIVED;
    }
}