package edu.npic.sps.features.analysis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashMap;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TotalCountResponse(
        long totalUserCount,
        long totalVehicleCount,
        long totalParkingSpaceCount,
        long totalParkingLotCount,
        long totalCompanyCount,
        long totalSiteCount,
        HashMap<String, List<String>> chartData
) {
}
