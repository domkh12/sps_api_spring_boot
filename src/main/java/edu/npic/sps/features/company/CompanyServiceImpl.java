package edu.npic.sps.features.company;

import edu.npic.sps.domain.City;
import edu.npic.sps.domain.Company;
import edu.npic.sps.domain.CompanyType;
import edu.npic.sps.features.city.CityRepository;
import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CompanyRequest;
import edu.npic.sps.features.companyType.CompanyTypeRepository;
import edu.npic.sps.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CompanyTypeRepository companyTypeRepository;
    private final CityRepository cityRepository;

    @Override
    public Page<CompanyResponse> filterCompany(String keywords, List<String> companyTypeUuids, List<String> cityUuids, int pageSize, int pageNo) {

        // validate pageNo and pageSize
        if (pageNo < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number must be greater than 0");
        } else if (pageSize < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page size must be greater than 0");
        }

        // sort by createdAt in descending order
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        // filter companies based on keywords, company types, and cities
        Page<Company> companies = companyRepository.filterCompany(
                keywords,
                cityUuids.isEmpty() ? null : cityUuids,
                companyTypeUuids.isEmpty() ? null : companyTypeUuids,
                pageable
        );

        // map the filtered companies to CompanyResponse DTOs
        return companies.map(companyMapper::toCompanyResponse);
    }

    @Override
    public CompanyResponse update(String uuid, CompanyRequest companyRequest) {
        Company company = companyRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );
        CompanyType companyType = companyTypeRepository.findByUuid(companyRequest.companyTypeUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company type not found!")
        );
        City city = cityRepository.findByUuid(companyRequest.cityUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );
        if (companyRepository.existsByCompanyNameIgnoreCaseAndUuidNot(companyRequest.companyName(), uuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists!");
        }
        companyMapper.updateFromCompanyRequest(companyRequest, company);
        company.setCity(city);
        company.setCompanyType(companyType);
        return companyMapper.toCompanyResponse(companyRepository.save(company));
    }

    @Override
    public CompanyResponse findByUuid(String uuid) {
        Company company = companyRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );
        return companyMapper.toCompanyResponse(company);
    }

    @Override
    public CompanyResponse createCompany(CompanyRequest companyRequest) {
        if (companyRepository.existsByCompanyNameIgnoreCase(companyRequest.companyName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists!");
        }
        CompanyType companyType = companyTypeRepository.findByUuid(companyRequest.companyTypeUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company type not found!")
        );
        City city = cityRepository.findByUuid(companyRequest.cityUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );
        Company company = companyMapper.fromCreateCompany(companyRequest);
        company.setCity(city);
        company.setSiteQty(0);
        company.setCompanyType(companyType);
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

        return companyList.stream().map(companyMapper::toCompanyNameResponse).toList();
    }

    @Override
    public void delete(String uuid) {
        Company company = companyRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );
        companyRepository.deleteByUuid(company.getUuid());
    }
}
