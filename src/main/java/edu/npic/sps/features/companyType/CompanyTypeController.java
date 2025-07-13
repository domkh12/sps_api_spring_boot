package edu.npic.sps.features.companyType;

import edu.npic.sps.features.companyType.dto.CompanyTypeRequest;
import edu.npic.sps.features.companyType.dto.CompanyTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company-types")
@RequiredArgsConstructor
@Tag(name = "Company Type Management", description = "APIs for managing company type")
public class CompanyTypeController {

    private final CompanyTypeService companyTypeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    CompanyTypeResponse findByUuid(@PathVariable String uuid){
        return companyTypeService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void delete(@PathVariable String uuid){
        companyTypeService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{uuid}")
    CompanyTypeResponse update(@PathVariable String uuid, @Valid @RequestBody CompanyTypeRequest companyTypeRequest){
        return companyTypeService.update(uuid, companyTypeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CompanyTypeResponse create(@Valid @RequestBody CompanyTypeRequest companyTypeRequest){
        return companyTypeService.create(companyTypeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<CompanyTypeResponse> findAll(){
        return  companyTypeService.findAll();
    }
}
