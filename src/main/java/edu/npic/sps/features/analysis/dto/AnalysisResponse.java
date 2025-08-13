package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AnalysisResponse(
        TotalStatResponse totalStats,
        List<AnalysisCompanyResponse> companies,
        List<AnalysisBranchResponse> branchData,
        List<HourlyOccupancyResponse> hourlyData,
        List<WeeklyOccupancyResponse> weeklyData,
        List<ParkingAreasUtilizationResponse> parkingAreasUtilization,
        List<ParkingAreasDetailsResponse> parkingAreasDetails,
        List<HourlyVehicleExitResponse> hourlyVehicleExitResponse
) {
}
