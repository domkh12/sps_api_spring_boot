package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeRequest;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/license-plate-types")
@RequiredArgsConstructor
@Tag(name = "License plate type Management", description = "APIs for managing license plate types")
public class LicensePlateTypeController {

    private final LicensePlateTypeService licensePlateTypeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    LicensePlateTypeResponse findByUuid(@PathVariable String uuid) {
        return licensePlateTypeService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUuid(@PathVariable String uuid){
        licensePlateTypeService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    LicensePlateTypeResponse updateByUuid(@PathVariable String uuid, @RequestBody LicensePlateTypeRequest licensePlateTypeRequest){
        return licensePlateTypeService.updateByUuid(uuid, licensePlateTypeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LicensePlateTypeResponse create(@RequestBody LicensePlateTypeRequest licensePlateTypeRequest){
        return licensePlateTypeService.create(licensePlateTypeRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<LicensePlateTypeResponse> findAll(){
        return licensePlateTypeService.findAll();
    }




}
