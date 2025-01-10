package edu.npic.sps.features.vehicle.dto;

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
        String licensePlateKhName,
        String licensePlateEngName,
        String vehicleMake,
        String color,
        String image,
        String vehicleModel,
        String vehicleDescription,
        UserDetailResponse user,
        VehicleTypeResponse vehicleType,
        LocalDateTime createdAt

) {
}
