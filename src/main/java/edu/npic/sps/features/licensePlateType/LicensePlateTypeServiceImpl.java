package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.domain.LicensePlateType;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeRequest;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import edu.npic.sps.mapper.LicensePlateTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicensePlateTypeServiceImpl implements LicensePlateTypeService{

    private final LicensePlateTypeRepository licensePlateTypeRepository;
    private final LicensePlateTypeMapper licensePlateTypeMapper;

    @Override
    public void delete(String uuid) {
        LicensePlateType licensePlateType = licensePlateTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License plate type not found!")
        );
        licensePlateTypeRepository.deleteByUuid(licensePlateType.getUuid());
    }

    @Override
    public LicensePlateTypeResponse updateByUuid(String uuid, LicensePlateTypeRequest licensePlateTypeRequest) {
        LicensePlateType licensePlateType = licensePlateTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License plate type not found!")
        );
        licensePlateTypeMapper.updateFromLicensePlateTypeRequest(licensePlateTypeRequest, licensePlateType);

        return licensePlateTypeMapper.toLicensePlateTypeResponse(licensePlateTypeRepository.save(licensePlateType));
    }

    @Override
    public LicensePlateTypeResponse create(LicensePlateTypeRequest licensePlateTypeRequest) {
        if (licensePlateTypeRepository.existsByNameIgnoreCase(licensePlateTypeRequest.name().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "License plate type name already exists!");
        }
        LicensePlateType licensePlateType = licensePlateTypeMapper.fromLicensePlateTypeRequest(licensePlateTypeRequest);
        licensePlateType.setUuid(UUID.randomUUID().toString());
        LicensePlateType licensePlateTypeSaved = licensePlateTypeRepository.save(licensePlateType);
        return licensePlateTypeMapper.toLicensePlateTypeResponse(licensePlateTypeSaved);
    }

    @Override
    public List<LicensePlateTypeResponse> findAll() {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<LicensePlateType>  licensePlateTypes = licensePlateTypeRepository.findAll(sort);
        return licensePlateTypes.stream().map(licensePlateTypeMapper::toLicensePlateTypeResponse).toList();
    }

}
