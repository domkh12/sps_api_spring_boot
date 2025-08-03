package edu.npic.sps.features.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.npic.sps.features.gender.dto.GenderResponse;
import edu.npic.sps.features.role.dto.RoleResponse;
import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.features.site.dto.SiteResponse;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDetailResponse(
        Integer id,
        String uuid,
        String fullName,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        String address,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt,
        List<RoleResponse> roles,
        String profileImage,
        String bannerImage,
        Boolean isOnline,
        String status,
        List<SiteResponse> sites,
        GenderResponse gender,
        SignUpMethodResponse signUpMethod,
        Boolean isVerified,
        Boolean isDeleted
) {
}
