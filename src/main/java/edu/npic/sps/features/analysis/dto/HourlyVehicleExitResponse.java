package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record HourlyVehicleExitResponse(
        String hour,
        Long occupied,
        Integer total
) {
}
