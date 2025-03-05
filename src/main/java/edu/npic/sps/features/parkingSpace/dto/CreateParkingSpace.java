package edu.npic.sps.features.parkingSpace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateParkingSpace(
        @NotBlank(message = "Parking label is required!")
        String label,
        String image,
        @NotNull(message = "Site Uuid is required!")
        String siteUuid,
        List<String> lotName
) {
}
