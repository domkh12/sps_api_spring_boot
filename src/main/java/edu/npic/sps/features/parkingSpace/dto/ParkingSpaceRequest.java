package edu.npic.sps.features.parkingSpace.dto;

import java.util.List;

public record ParkingSpaceRequest(
        String label,
        String image,
        String siteUuid
) {
}
