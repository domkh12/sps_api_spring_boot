package edu.npic.sps.features.user.dto;

import jakarta.validation.constraints.NotNull;

public record IsOnlineRequest(
        @NotNull(message = "isOnline cannot be null!")
        Boolean isOnline
) {
}
