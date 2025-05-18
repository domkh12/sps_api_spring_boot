package edu.npic.sps.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record JwtResponse(

        String tokenType,
        String accessToken,
        Boolean required2FA
) {
}
