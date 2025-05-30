package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/license-plate-provinces")
@RequiredArgsConstructor
public class LicensePlateProvinceController {

    private final LicensePlateProvinceService licensePlateProvinceService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<LicensePlateProvinceResponse> findAll(){
        return licensePlateProvinceService.findAll();
    }


}
