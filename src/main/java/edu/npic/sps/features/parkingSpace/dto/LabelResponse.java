package edu.npic.sps.features.parkingSpace.dto;

import lombok.Builder;

@Builder
public record LabelResponse(
        String uuid,
        String label
) {
}
