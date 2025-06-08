package edu.npic.sps.features.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String companyName,
        String address,
        @NotBlank(message = "Company type uuid is required")
        String companyTypeUuid,
        String image
) {
}
