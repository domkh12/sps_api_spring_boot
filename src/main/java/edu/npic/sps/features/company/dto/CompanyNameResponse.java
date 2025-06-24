package edu.npic.sps.features.company.dto;

import java.time.LocalDateTime;

public record CompanyNameResponse(
        String uuid,
        String companyName,
        LocalDateTime createdAt
) {
}
