package edu.npic.sps.features.site;

import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteRequest;
import edu.npic.sps.features.site.dto.SiteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Site Management", description = "APIs for managing sites")
public class SiteController {

    private final SiteService siteService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    SiteResponse findByUuid(@PathVariable String uuid){
        return siteService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    Page<SiteResponse> filter(@RequestParam(required = false, defaultValue = "1") int pageNo,
                        @RequestParam(required = false, defaultValue = "20") int pageSize,
                        @RequestParam(required = false, defaultValue = "") String keywords,
                        @RequestParam(required = false, defaultValue = "") String cityId,
                        @RequestParam(required = false, defaultValue = "") String siteTypeId,
                        @RequestParam(required = false, defaultValue = "") String companyId) {
        return siteService.filter(pageNo, pageSize, keywords, cityId, siteTypeId, companyId);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid){
        siteService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    SiteResponse update(@PathVariable String uuid, @RequestBody @Valid SiteRequest siteRequest){
        return siteService.update(uuid, siteRequest);
    }

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

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SiteResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize ){
        return siteService.findAll(pageNo, pageSize);
    }

}
