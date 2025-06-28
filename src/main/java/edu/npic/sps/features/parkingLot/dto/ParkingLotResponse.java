package edu.npic.sps.features.parkingLot.dto;

import edu.npic.sps.features.parkingSpace.dto.ParkingNameResponse;
import lombok.Builder;

@Builder
public record ParkingLotResponse(
        String uuid,
        String lotName,
        Boolean isAvailable,
        ParkingNameResponse parkingSpace
) {
}
