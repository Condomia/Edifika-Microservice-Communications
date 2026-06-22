package com.edifika.communication.communication.domain.model.entities;

import com.edifika.communication.shared.domain.model.entity.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Table(name = "announcement_reads")
public class AnnouncementRead extends AuditableModel {

    @Column(nullable = false)
    private Long announcementId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Date readAt;

    protected AnnouncementRead() {
    }

    public AnnouncementRead(Long announcementId, Long userId) {
        this.announcementId = announcementId;
        this.userId = userId;
        this.readAt = new Date();
    }
}