package edu.npic.sps.features.vehicle.dto;

public record CameraRequest(
        String numberPlate,
        String provincePlate,
        String vehicleModel,
        String vehicleMake,
        String branchUuid,
        String color,
        String image,
        String imageCheckIn,
        String imageCheckOut
) {
}
