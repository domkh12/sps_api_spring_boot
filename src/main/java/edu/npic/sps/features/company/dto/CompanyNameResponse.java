package edu.npic.sps.features.company.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.npic.sps.features.site.dto.SiteNameResponse;

import java.time.LocalDateTime;
import java.util.List;

public record CompanyNameResponse(
        String uuid,
        String companyName,
        List<SiteNameResponse> sites,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt
) {
}
