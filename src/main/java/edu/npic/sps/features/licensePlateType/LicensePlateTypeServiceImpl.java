package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.domain.LicensePlateType;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import edu.npic.sps.mapper.LicensePlateTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicensePlateTypeServiceImpl implements LicensePlateTypeService{

    private final LicensePlateTypeRepository licensePlateTypeRepository;
    private final LicensePlateTypeMapper licensePlateTypeMapper;

    @Override
    public List<LicensePlateTypeResponse> findAll() {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<LicensePlateType>  licensePlateTypes = licensePlateTypeRepository.findAll(sort);
        return licensePlateTypes.stream().map(licensePlateTypeMapper::toLicensePlateTypeResponse).toList();
    }

}
