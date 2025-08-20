package edu.npic.sps.features.user.dto;

import edu.npic.sps.features.gender.dto.GenderResponse;

import java.time.LocalDate;

public record ReportUserResponse(
        String fullName,
        GenderResponse gender,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber
) {
}
