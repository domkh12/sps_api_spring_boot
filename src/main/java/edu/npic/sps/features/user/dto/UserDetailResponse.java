package edu.npic.sps.features.user.dto;

import edu.npic.sps.features.gender.dto.GenderResponse;
import edu.npic.sps.features.role.dto.RoleResponse;
import edu.npic.sps.features.site.dto.SiteResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDetailResponse(

        String uuid,
        String fullName,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        String address,
        LocalDateTime createdAt,
        List<RoleResponse> roles,
        String profileImage,
        String bannerImage,
        Boolean isOnline,
        String status,
        List<SiteResponse> sites,
        GenderResponse gender
) {
}