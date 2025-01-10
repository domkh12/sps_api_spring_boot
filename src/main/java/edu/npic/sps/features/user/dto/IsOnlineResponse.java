package edu.npic.sps.features.user.dto;

import lombok.Builder;

@Builder
public record IsOnlineResponse(
        String uuid,
        Boolean isOnline
) {
}
