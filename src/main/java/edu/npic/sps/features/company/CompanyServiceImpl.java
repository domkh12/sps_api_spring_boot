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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse findByUuid(String uuid) {
        Company company = companyRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );
        return companyMapper.toCompanyResponse(company);
    }

    @Override
    public CompanyResponse createCompany(CreateCompany createCompany) {
        if (companyRepository.existsByCompanyNameIgnoreCase(createCompany.companyName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists!");
        }
        Company company = companyMapper.fromCreateCompany(createCompany);
        company.setUuid(UUID.randomUUID().toString());
        company.setCreatedAt(LocalDateTime.now());
        companyRepository.save(company);
        return companyMapper.toCompanyResponse(company);
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
