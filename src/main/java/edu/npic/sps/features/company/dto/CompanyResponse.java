package edu.npic.sps.features.company.dto;

import edu.npic.sps.features.site.dto.SiteNameResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record CompanyResponse(
        String uuid,
        String companyName
) {
}
