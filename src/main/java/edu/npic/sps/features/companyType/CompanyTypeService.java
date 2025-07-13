package edu.npic.sps.features.companyType;

import edu.npic.sps.domain.CompanyType;
import edu.npic.sps.features.companyType.dto.CompanyTypeRequest;
import edu.npic.sps.features.companyType.dto.CompanyTypeResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CompanyTypeService {
    CompanyTypeResponse findByUuid(String uuid);

    void delete(String uuid);

    CompanyTypeResponse update(String uuid, @Valid CompanyTypeRequest companyTypeRequest);

    CompanyTypeResponse create(@Valid CompanyTypeRequest companyTypeRequest);

    List<CompanyTypeResponse> findAll();
}
