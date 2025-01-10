package edu.npic.sps.features.parkingLot.dto;

import edu.npic.sps.features.vehicle.dto.VehicleResponse;

import java.time.LocalDateTime;

public record ParkingSlotDetailResponse(

        LocalDateTime timeIn,
        LocalDateTime timeOut,
        VehicleResponse vehicle,
        ParkingLotResponse parkingSlot

) {
}
