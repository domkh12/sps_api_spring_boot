package edu.npic.sps.features.analysis;

import edu.npic.sps.features.analysis.dto.TotalCountResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis Management", description = "APIs for managing analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/total-counts")
    @ResponseStatus(HttpStatus.OK)
    TotalCountResponse totalCount(){
        return analysisService.totalCount();
    }
}
