package edu.npic.sps.features.parkingLotDetail.dto;

import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParkingDetailResponse(
        String uuid,
        ParkingLotResponse parkingLot,
        VehicleResponse vehicle,
        ParkingSpaceResponse parkingSpace,
        LocalDateTime timeIn,
        LocalDateTime timeOut,
        Boolean isParking,
        Long durationHours,
        LocalDateTime createdAt
) {
}
