package edu.npic.sps.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangePasswordRequest(
        @JsonProperty("old_password")
        String oldPassword,
        @JsonProperty("new_password")
        String newPassword
) {
}
