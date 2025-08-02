package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record AnalysisCompanyResponse(
    Integer id,
    String name,
    Integer branches,
    Integer totalAreas,
    Integer totalSlots,
    Integer occupiedSlots,
    Double avgStayTime,
    String color
) {
}
