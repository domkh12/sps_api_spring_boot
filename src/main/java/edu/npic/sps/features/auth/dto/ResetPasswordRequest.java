package edu.npic.sps.features.auth.dto;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {
}
