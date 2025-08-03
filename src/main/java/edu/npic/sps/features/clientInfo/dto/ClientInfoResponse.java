package edu.npic.sps.features.clientInfo.dto;

import java.time.LocalDateTime;

public record ClientInfoResponse(
        Integer id,
        String ipAddress,
        String browser,
        String deviceTye,
        String loginType,
        String operatingSystem,
        LocalDateTime createdAt
) {
}
