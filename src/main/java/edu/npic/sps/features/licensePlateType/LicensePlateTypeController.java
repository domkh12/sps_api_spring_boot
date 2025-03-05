package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/license-plate-types")
@RequiredArgsConstructor
public class LicensePlateTypeController {

    private final LicensePlateTypeService licensePlateTypeService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<LicensePlateTypeResponse> findAll(){
        return licensePlateTypeService.findAll();
    }




}
