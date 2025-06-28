package edu.npic.sps.features.parkingSpace.dto;

import lombok.Builder;

@Builder
public record ParkingNameResponse(
        String uuid,
        String label
) {
}
