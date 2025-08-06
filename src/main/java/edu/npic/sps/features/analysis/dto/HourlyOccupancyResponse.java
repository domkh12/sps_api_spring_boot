package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record HourlyOccupancyResponse(
        String hour,
        Integer occupied,
        Integer total
) {
}
