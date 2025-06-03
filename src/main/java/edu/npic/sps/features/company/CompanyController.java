package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
@Tag(name = "Company Management", description = "APIs for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    CompanyResponse findByUuid(@PathVariable String uuid) {
        return companyService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    void delete(@PathVariable String uuid) {
        companyService.delete(uuid);
    }

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
