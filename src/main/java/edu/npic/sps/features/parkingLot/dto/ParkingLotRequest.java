package edu.npic.sps.features.parkingLot.dto;

public record ParkingLotRequest(
        String lotName,
        String parkingSpaceUuid
) {
}
