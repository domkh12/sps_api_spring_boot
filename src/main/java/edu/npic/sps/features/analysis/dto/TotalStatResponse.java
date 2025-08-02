package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record TotalStatResponse(
        Integer totalUserCount,
        Integer totalVehicleCount,
        Integer totalParkingAreasCount,
        Integer totalParkingSlotsCount,
        Integer totalCompanies,
        Integer totalBranches,
        Double occupancyRate,
        Double averageStayTime
) {
}
