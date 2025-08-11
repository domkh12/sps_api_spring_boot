package edu.npic.sps.features.analysis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TotalStatResponse(
        Integer totalUserCount,
        Integer totalVehicleCount,
        Integer totalParkingAreasCount,
        Integer totalParkingSlotsCount,
        Integer activeSessionCount,
        Integer checkInCount,
        Integer totalCompanies,
        Integer totalBranches,
        Double occupancyRate,
        Double averageStayTime

) {
}
