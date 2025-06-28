package edu.npic.sps.features.company.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String companyName,
        String companyAddress,
        @NotBlank(message = "Company type uuid is required")
        String companyTypeUuid,
        @NotBlank(message = "City uuid is required")
        String cityUuid,
        String image,
        LocalDate establishedDate
) {
}
