package edu.npic.sps.features.siteType;

import edu.npic.sps.features.siteType.dto.SiteTypeRequest;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/site-types")
@RequiredArgsConstructor
@Tag(name = "SiteType Management", description = "APIs for managing site types")
public class SiteTypeController {

    private final SiteTypeService siteTypeService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    SiteTypeResponse findByUuid(@PathVariable String uuid){
        return siteTypeService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid){
        siteTypeService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    SiteTypeResponse updateByUuid(@PathVariable String uuid, @RequestBody SiteTypeRequest siteTypeRequest){
        return siteTypeService.updateByUuid(uuid, siteTypeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SiteTypeResponse create(@Valid @RequestBody SiteTypeRequest siteTypeRequest){
        return siteTypeService.create(siteTypeRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER' ,'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<SiteTypeResponse> findAll(){
        return siteTypeService.findAll();
    }
}
