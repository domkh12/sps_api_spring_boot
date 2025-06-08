package edu.npic.sps.features.companyType.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CompanyTypeRequest(
        String name
) {
}
