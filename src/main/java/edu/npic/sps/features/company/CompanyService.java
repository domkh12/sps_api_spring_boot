package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    CompanyResponse findByUuid(String uuid);

    CompanyResponse createCompany(CreateCompany createCompany);

    Page<CompanyResponse> findAll(int pageNo, int pageSize);

    List<CompanyNameResponse> findAllNames();

    void delete(String uuid);
}
