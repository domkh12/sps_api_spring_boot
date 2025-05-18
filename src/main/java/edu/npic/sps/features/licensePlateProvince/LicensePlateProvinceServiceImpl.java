package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.domain.LicensePlateProvince;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import edu.npic.sps.mapper.LicensePlateProvinceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicensePlateProvinceServiceImpl implements LicensePlateProvinceService{

    private final LicensePlateProvinceRepository licensePlateProvinceRepository;
    private final LicensePlateProvinceMapper licensePlateProvinceMapper;

    @Override
    public List<LicensePlateProvinceResponse> findAll() {
        List<LicensePlateProvince> licensePlateProvince = licensePlateProvinceRepository.findAll();
        return licensePlateProvince.stream().map(licensePlateProvinceMapper::toLicensePlateProvinceResponse).toList();
    }
}
