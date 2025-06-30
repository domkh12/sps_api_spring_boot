package edu.npic.sps.features.site.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSite(
        @NotBlank(message = "Site name cannot be blank")
        String siteName,
        String siteAddress,
        String image,
        @NotNull(message = "City UUID cannot be null")
        String cityUuid,
        @NotNull(message = "Site type UUID cannot be null")
        String siteTypeUuid,
        @NotNull(message = "Company UUID cannot be null")
        String companyUuid
) {
}
