package edu.npic.sps.features.user.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.List;

public record UpdateUserRequest(
        String fullName,
        @Email(message = "email wrong format")
        String email,
        String genderId,
        String address,
        LocalDate dateOfBirth,
        String phoneNumber,
        String profileImage,
        String bannerImage,
        Boolean isDeleted,
        Boolean isVerified,
        List<String> roleId,
        List<String> branchId
) {
}
