package edu.npic.sps.features.companyType.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CompanyTypeResponse(
    String uuid,
    String name,
    @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
    LocalDateTime createdAt
) {
}
