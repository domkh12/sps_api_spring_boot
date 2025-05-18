package edu.npic.sps.features.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VehicleRequest(

        String numberPlate,
        String vehicleMake,
        String vehicleModel,
        String color,
        String image,
        String userId,
        String vehicleTypeId,
        String licensePlateTypeId,
        String licensePlateProvinceId

) {
}
