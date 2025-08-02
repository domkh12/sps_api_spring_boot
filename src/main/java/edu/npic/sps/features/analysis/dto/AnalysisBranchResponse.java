package edu.npic.sps.features.analysis.dto;

import lombok.Builder;

@Builder
public record AnalysisBranchResponse(
        String name,
        Integer areas,
        Integer slots,
        Integer occupied,
        Integer efficiency
) {
}
