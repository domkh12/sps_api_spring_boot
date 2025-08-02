package edu.npic.sps.features.parkingLotDetail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime timeIn,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime timeOut,
        Boolean isParking,
        Long durations,
        String image,
        String imageCheckIn,
        String imageCheckOut,
        Boolean isCheckIn,
        Boolean isCheckOut,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt
) {
}
