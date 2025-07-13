package edu.npic.sps.features.companyType;

import edu.npic.sps.domain.CompanyType;
import edu.npic.sps.features.companyType.dto.CompanyTypeRequest;
import edu.npic.sps.features.companyType.dto.CompanyTypeResponse;
import edu.npic.sps.mapper.CompanyTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyTypeServiceImpl implements CompanyTypeService {

    private final CompanyTypeRepository companyTypeRepository;
    private final CompanyTypeMapper companyTypeMapper;

    @Override
    public CompanyTypeResponse findByUuid(String uuid) {
        CompanyType companyType = companyTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company type not found!")
        );
        return companyTypeMapper.toCompanyTypeResponse(companyType);
    }

    @Override
    public void delete(String uuid) {
        CompanyType companyType = companyTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company type not found!")
        );
        companyTypeRepository.deleteByUuid(companyType.getUuid());
    }

    @Override
    public CompanyTypeResponse update(String uuid, CompanyTypeRequest companyTypeRequest) {
        CompanyType companyType = companyTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company type not found!")
        );
        if (companyTypeRepository.existsByNameIgnoreCaseAndUuidNot(companyTypeRequest.name().trim(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company type name already exists!");
        }
        companyTypeMapper.updateFromCompanyTypeRequest(companyTypeRequest, companyType);
        return companyTypeMapper.toCompanyTypeResponse(companyTypeRepository.save(companyType));
    }

    @Override
    public CompanyTypeResponse create(CompanyTypeRequest companyTypeRequest) {
        if (companyTypeRepository.existsByNameIgnoreCase(companyTypeRequest.name().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company type name already exists!");
        }
        CompanyType companyType = companyTypeMapper.fromCompanyTypeRequest(companyTypeRequest);
        companyType.setUuid(UUID.randomUUID().toString());
        companyType.setCreatedAt(LocalDateTime.now());
        CompanyType savedCompanyType = companyTypeRepository.save(companyType);
        return companyTypeMapper.toCompanyTypeResponse(savedCompanyType);
    }

    @Override
    public List<CompanyTypeResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<CompanyType> companyTypes = companyTypeRepository.findAll(sort);
        return companyTypes.stream().map(companyTypeMapper::toCompanyTypeResponse).toList();
    }
}
