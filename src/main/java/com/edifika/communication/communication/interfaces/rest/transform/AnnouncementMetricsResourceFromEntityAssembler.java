package com.edifika.communication.communication.interfaces.rest.transform;

import com.edifika.communication.communication.interfaces.rest.resources.AnnouncementMetricsResource;

public class AnnouncementMetricsResourceFromEntityAssembler {

    public static AnnouncementMetricsResource toResource(
            Long announcementId,
            Long totalResidents,
            Long readResidents
    ) {

        double reachPercentage = totalResidents == 0
                ? 0.0
                : ((double) readResidents / totalResidents) * 100;

        return new AnnouncementMetricsResource(
                announcementId,
                totalResidents,
                readResidents,
                reachPercentage
        );
    }
}