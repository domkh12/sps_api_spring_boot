package edu.npic.sps.features.company;

import edu.npic.sps.domain.Company;
import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import edu.npic.sps.features.siteType.SiteTypeRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse createCompany(CreateCompany createCompany) {

        return null;
    }

    @Override
    public List<CompanyResponse> findAll() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(company -> companyMapper.toCompanyResponse(company)).toList();
    }

    @Override
    public List<CompanyNameResponse> findAllNames() {
        List<Company> companyList = companyRepository.findAll();

        return companyList.stream().map(
                company -> companyMapper.toCompanyNameResponse(company)
        ).toList();
    }

    @Override
    public void delete(String uuid) {
        Company company = companyRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );

        companyRepository.delete(company);
    }

}
