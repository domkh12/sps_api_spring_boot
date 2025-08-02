package edu.npic.sps.features.analysis;

import edu.npic.sps.features.analysis.dto.AnalysisResponse;
import edu.npic.sps.features.analysis.dto.TotalCountResponse;

public interface AnalysisService {
    AnalysisResponse getAnalysis();

    TotalCountResponse totalCount();
}
