package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    CompanyResponse createCompany(@RequestBody CreateCompany createCompany) {
        return companyService.createCompany(createCompany);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CompanyResponse> findAll(){
        return companyService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/names")
    List<CompanyNameResponse> findAllNames() {
        return companyService.findAllNames();
    }
}
