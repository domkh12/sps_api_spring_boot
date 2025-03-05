package edu.npic.sps.features.siteType;

import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/site-types")
@RequiredArgsConstructor
public class SiteTypeController {

    private final SiteTypeService siteTypeService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER' ,'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<SiteTypeResponse> findAll(){
        return siteTypeService.findAll();
    }
}
