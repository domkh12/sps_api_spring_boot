package edu.npic.sps.features.vehicle.dto;

import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
public record VehicleResponse(

        String uuid,
        String numberPlate,
        String vehicleMake,
        String color,
        String image,
        String vehicleModel,
        UserDetailResponse user,
        VehicleTypeResponse vehicleType,
        LicensePlateProvinceResponse licensePlateProvince,
        LicensePlateTypeResponse licensePlateType,
        LocalDateTime createdAt

) {
}
