package edu.npic.sps.features.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateVehicle(
        @NotBlank(message = "numberPlate is required")
        @Size(max = 50, message = "numberPlate must not exceed 50 characters")
        String numberPlate,
        @NotBlank(message = "licensePlateKhName is required")
        @Size(max = 200, message = "vehicleMake must not exceed 200 characters")
        String vehicleMake,
        @Size(max = 200, message = "vehicleModel must not exceed 200 characters")
        String vehicleModel,
        @Size(max = 30, message = "color must not exceed 30 characters")
        String color,
        String image,
        @NotBlank(message = "userId is required")
        String userId,
        @NotBlank(message = "vehicleTypeId is required")
        String vehicleTypeId,
        @NotBlank(message = "licensePlateTypeId is required")
        String licensePlateTypeId,
        @NotBlank(message = "LicensePlateProvinceId is required!")
        String licensePlateProvinceId
) {
}
