package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CompanyRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    CompanyResponse update(String uuid, CompanyRequest companyRequest);

    CompanyResponse findByUuid(String uuid);

    CompanyResponse createCompany(CompanyRequest companyRequest);

    Page<CompanyResponse> findAll(int pageNo, int pageSize);

    List<CompanyNameResponse> findAllNames();

    void delete(String uuid);

    Page<CompanyResponse> filterCompany(String keywords, List<String> companyTypeUuid, List<String> cityUuid, int pageSize, int pageNo);
}
