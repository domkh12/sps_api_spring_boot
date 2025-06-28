package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyRequest;
import edu.npic.sps.features.company.dto.CompanyResponse;
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
@RequestMapping("/api/v1/companies")
@Tag(name = "Company Management", description = "APIs for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    CompanyResponse update(@PathVariable String uuid,@Valid @RequestBody CompanyRequest companyRequest) {
        return companyService.update(uuid, companyRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    CompanyResponse findByUuid(@PathVariable String uuid) {
        return companyService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid) {
        companyService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompanyResponse createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        return companyService.createCompany(companyRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<CompanyResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
        return companyService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/names")
    List<CompanyNameResponse> findAllNames() {
        return companyService.findAllNames();
    }
}
