package edu.npic.sps.features.site.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSite(
        @NotBlank(message = "Site name cannot be blank")
        @Size(min = 2, max = 500, message = "Site name must be between 3 and 500 characters")
        String siteName,
        @NotBlank(message = "siteAddress cannot be blank")
        @Size(max = 500, message = "Site address must be no longer than 500 characters")
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
