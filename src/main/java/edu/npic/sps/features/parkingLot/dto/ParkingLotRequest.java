package edu.npic.sps.features.parkingLot.dto;

import java.util.List;

public record ParkingLotRequest(
        List<String> lotName,
        String parkingSpaceUuid
) {
}
