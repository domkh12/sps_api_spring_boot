package edu.npic.sps.features.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponse(

        String tokenType, // Bearer
        String accessToken

) {
}
