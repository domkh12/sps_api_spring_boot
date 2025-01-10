package edu.npic.sps.features.parkingSpace.dto;

import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ParkingSpaceResponse(
        String uuid,
        String label,
        Integer lotQty,
        Integer filled,
        Integer empty,
        String lastUpdate,
        List<ParkingLotResponse> parkingLots
) {
}
