package edu.npic.sps.features.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import edu.npic.sps.features.site.dto.SiteNameResponse;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record VehicleResponse(

        String uuid,
        String numberPlate,
        String vehicleMake,
        String color,
        String image,
        String vehicleModel,
        UserDetailResponse user,
        List<SiteNameResponse> sites,
        VehicleTypeResponse vehicleType,
        LicensePlateProvinceResponse licensePlateProvince,
        LicensePlateTypeResponse licensePlateType,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt

) {
}
