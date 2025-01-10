package edu.npic.sps.features.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateVehicle(
        @NotBlank(message = "numberPlate is required")
        @Size(max = 50, message = "numberPlate must not exceed 50 characters")
        String numberPlate,
        @NotBlank(message = "licensePlateKhName is required")
        @Size(max = 200, message = "licensePlateKhName must not exceed 200 characters")
        String licensePlateKhName,
        @NotBlank(message = "licensePlateEngName is required")
        @Size(max = 200, message = "licensePlateEngName must not exceed 200 characters")
        String licensePlateEngName,
        @Size(max = 200, message = "vehicleMake must not exceed 200 characters")
        String vehicleMake,
        @Size(max = 200, message = "vehicleModel must not exceed 200 characters")
        String vehicleModel,
        @Size(max = 30, message = "color must not exceed 30 characters")
        String color,
        @Pattern(regexp = "^(http|https)://.*$", message = "image must be a valid URL")
        String image,
        @NotBlank(message = "userId is required")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "userId must be a valid UUID")
        String userId,
        @NotBlank(message = "vehicleTypeId is required")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "vehicleTypeId must be a valid UUID")
        String vehicleTypeId
) {
}
