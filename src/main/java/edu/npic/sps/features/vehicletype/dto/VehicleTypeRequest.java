package edu.npic.sps.features.vehicletype.dto;

import jakarta.validation.constraints.NotBlank;

public record VehicleTypeRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
