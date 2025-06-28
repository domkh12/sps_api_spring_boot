package edu.npic.sps.features.city.dto;

import jakarta.validation.constraints.NotNull;

public record CityRequest(
        @NotNull(message = "Name is required")
        String name
) {
}
