package edu.npic.sps.features.auth.dto;

import lombok.Builder;

@Builder
public record MessageResponse(
        String message
) {
}
