package edu.npic.sps.features.company.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.npic.sps.features.companyType.dto.CompanyTypeResponse;
import edu.npic.sps.features.site.dto.SiteNameResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CompanyResponse(
        String uuid,
        String companyName,
        String companyAddress,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt,
        CompanyTypeResponse companyType
) {
}
