package edu.npic.sps.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Token is required")
        String token
) {
}
