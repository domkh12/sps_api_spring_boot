package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record ParkingAreasDetailsResponse(
        Integer id,
        String name,
        Integer totalSlots,
        Integer available,
        Integer occupied,
        Double occupancyRate,
        String barColor
) {
}
