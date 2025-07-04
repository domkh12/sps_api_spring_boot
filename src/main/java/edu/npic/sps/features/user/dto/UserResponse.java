package edu.npic.sps.features.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserResponse(

        String uuid,
        String fullName,
        String email,
        String phoneNumber,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt

) {
}
