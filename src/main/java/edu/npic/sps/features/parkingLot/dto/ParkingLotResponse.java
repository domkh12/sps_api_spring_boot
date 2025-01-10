package edu.npic.sps.features.parkingLot.dto;

import lombok.Builder;

@Builder
public record ParkingLotResponse(
        String uuid,
        String lotName,
        Boolean isAvailable
) {
}
