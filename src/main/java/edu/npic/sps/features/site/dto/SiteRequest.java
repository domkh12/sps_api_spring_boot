package edu.npic.sps.features.site.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SiteRequest(
        @NotBlank(message = "Site name cannot be blank")
        String siteName,
        String siteAddress,
        String image,
        @NotBlank(message = "City UUID cannot be blank")
        String cityUuid,
        @NotBlank(message = "Site type UUID cannot be blank")
        String siteTypeUuid,
        @NotBlank(message = "Company UUID cannot be blank")
        String companyUuid
) {
}
