package edu.npic.sps.features.companyType.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyTypeRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
