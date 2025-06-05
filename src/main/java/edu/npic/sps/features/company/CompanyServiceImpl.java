package edu.npic.sps.features.company;

import edu.npic.sps.domain.Company;
import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import edu.npic.sps.features.siteType.SiteTypeRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<CompanyResponse> findAll(int pageNo, int pageSize) {

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Company> companies = companyRepository.findAll(pageRequest);

        return companies.map(companyMapper::toCompanyResponse);
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
