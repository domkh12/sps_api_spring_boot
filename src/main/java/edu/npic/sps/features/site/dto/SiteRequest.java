package edu.npic.sps.features.site.dto;

import jakarta.validation.constraints.Size;

public record SiteRequest(
        @Size(min = 2, max = 500, message = "Site name must be between 3 and 500 characters")
        String siteName,
        @Size(max = 500, message = "Site address must be no longer than 500 characters")
        String siteAddress,
        String image,
        String cityUuid,
        String siteTypeUuid,
        String companyUuid
) {
}
