package edu.npic.sps.features.company;

import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    CompanyResponse createCompany(CreateCompany createCompany);

    List<CompanyResponse> findAll();

    List<CompanyNameResponse> findAllNames();

    void delete(String uuid);
}
