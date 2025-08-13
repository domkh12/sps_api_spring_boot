package edu.npic.sps.features.site.dto;

import edu.npic.sps.features.company.dto.CompanyListForParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.ParkingNameResponse;

import java.util.List;

public record BranchListResponse(
        String uuid,
        String siteName,
        String image,
        CompanyListForParkingSpace company,
        List<ParkingNameResponse> parkingSpaces
) {
}
