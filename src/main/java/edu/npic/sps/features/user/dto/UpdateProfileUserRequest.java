package edu.npic.sps.features.user.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UpdateProfileUserRequest(
        String fullName,
        LocalDate dateOfBirth,
        String genderUuid,
        @Email(message = "Email wrong format")
        String email,
        String profileImage,
        String phoneNumber,
        String address
) {
}
