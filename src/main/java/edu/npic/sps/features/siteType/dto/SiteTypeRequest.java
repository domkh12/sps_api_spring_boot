package edu.npic.sps.features.siteType.dto;

import jakarta.validation.constraints.NotNull;

public record SiteTypeRequest(
        @NotNull(message = "Name is required")
        String name
) {
}
