package edu.npic.sps.features.city.dto;

import java.time.LocalDateTime;

public record CityResponse(
        String uuid,
        String name,
        LocalDateTime createdAt
) {
}
