package edu.npic.sps.features.parkingLot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.npic.sps.features.parkingSpace.dto.ParkingNameResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParkingLotResponse(
        String uuid,
        String lotName,
        Boolean isAvailable,
        ParkingNameResponse parkingSpace,
        String image,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt
) {
}
