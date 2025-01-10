package edu.npic.sps.features.parkingSpace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateParking(
        @NotBlank(message = "Parking name is required!")
        String label,
        @NotNull(message = "Parking slot is required!")
        List<String> parkingSlotsName
) {
}
