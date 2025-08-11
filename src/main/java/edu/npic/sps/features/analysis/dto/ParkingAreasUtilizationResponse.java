package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record ParkingAreasUtilizationResponse(
        String name,
        Integer value,
        String color
) {
}
