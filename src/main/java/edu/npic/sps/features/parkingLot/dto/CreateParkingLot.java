package edu.npic.sps.features.parkingLot.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateParkingLot(
        @NotBlank(message = "lotName is required!")
        List<String> lotName,
        @NotBlank(message = "parkingSpaceUuid is required!")
        String parkingSpaceUuid
) {
}
