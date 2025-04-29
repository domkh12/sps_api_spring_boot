package edu.npic.sps.features.site.dto;

import edu.npic.sps.features.city.dto.CityResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;

import java.time.LocalDateTime;

public record SiteResponse(
        String uuid,
        String siteName,
        String siteAddress,
        LocalDateTime createdAt,
        CityResponse city,
        String image,
        SiteTypeResponse siteType,
        CompanyResponse company
) {
}
