package edu.npic.sps.features.parkingLot.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateCarParking(
        @NotBlank(message = "slotName is required!")
        String alias_slot,
        @NotBlank(message = "plateNumber is required!")
        String plateNumber,
        @NotBlank(message = "isAvailable is required!")
        Boolean isAvailable,
        @NotBlank(message = "timeIn is required!")
        LocalDateTime timeIn,
        LocalDateTime timeOut,
        @NotBlank(message = "userUuid is required!")
        String userUuid
) {
}
