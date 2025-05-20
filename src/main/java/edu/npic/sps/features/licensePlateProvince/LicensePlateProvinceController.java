package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceRequest;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/license-plate-provinces")
@RequiredArgsConstructor
@Tag(name = "License plate provinces Management", description = "APIs for managing license plate provinces")
public class LicensePlateProvinceController {

    private final LicensePlateProvinceService licensePlateProvinceService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUuid(@PathVariable String uuid){
        licensePlateProvinceService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    LicensePlateProvinceResponse updateByUuid(@PathVariable String uuid, @Valid @RequestBody LicensePlateProvinceRequest licensePlateProvinceRequest){
        return licensePlateProvinceService.updateByUuid(uuid, licensePlateProvinceRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LicensePlateProvinceResponse create(@Valid @RequestBody LicensePlateProvinceRequest licensePlateProvinceRequest){
        return licensePlateProvinceService.create(licensePlateProvinceRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<LicensePlateProvinceResponse> findAll(){
        return licensePlateProvinceService.findAll();
    }


}
