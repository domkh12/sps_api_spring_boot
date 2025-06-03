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
        @NotNull(message = "Lot name is required!")
        List<String> lotName
) {
}
