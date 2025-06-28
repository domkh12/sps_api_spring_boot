package edu.npic.sps.features.parkingLot.dto;

import java.util.List;

public record CreateMultipleSlot(
        List<ParkingLotRequest> slots
) {
}
