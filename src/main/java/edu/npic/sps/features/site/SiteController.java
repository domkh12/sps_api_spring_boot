package edu.npic.sps.features.site;

import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sites")
public class SiteController {

    private final SiteService siteService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SiteResponse createSite(@Valid @RequestBody CreateSite createSite){
        return siteService.createSite(createSite);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    List<SiteResponse> findAllByUserRole(){
        return siteService.findAllByUserRole();
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SiteResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false, defaultValue = "") String searchTerm ){
        return siteService.findAll(pageNo, pageSize, searchTerm);
    }

}
