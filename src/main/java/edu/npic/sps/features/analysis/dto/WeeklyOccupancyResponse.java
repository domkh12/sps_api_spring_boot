package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record WeeklyOccupancyResponse(
        String day,
        Integer occupancy,
        Double avgStayTime
) {
}
