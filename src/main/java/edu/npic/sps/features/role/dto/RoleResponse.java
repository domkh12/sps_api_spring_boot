package edu.npic.sps.features.role.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RoleResponse(
        String uuid,
        String name
) {
}
