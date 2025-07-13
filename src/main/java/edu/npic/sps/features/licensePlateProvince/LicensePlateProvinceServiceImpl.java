package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.domain.LicensePlateProvince;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceRequest;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import edu.npic.sps.mapper.LicensePlateProvinceMapper;
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
public class LicensePlateProvinceServiceImpl implements LicensePlateProvinceService{

    private final LicensePlateProvinceRepository licensePlateProvinceRepository;
    private final LicensePlateProvinceMapper licensePlateProvinceMapper;

    @Override
    public LicensePlateProvinceResponse findByUuid(String uuid) {
        LicensePlateProvince licensePlateProvince = licensePlateProvinceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License Plate Province Not Found!")
        );
        return licensePlateProvinceMapper.toLicensePlateProvinceResponse(licensePlateProvince);
    }

    @Override
    public void delete(String uuid) {
        LicensePlateProvince licensePlateProvince = licensePlateProvinceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License plate province not found!")
        );
        licensePlateProvinceRepository.deleteByUuid(licensePlateProvince.getUuid());
    }

    @Override
    public LicensePlateProvinceResponse updateByUuid(String uuid, LicensePlateProvinceRequest licensePlateProvinceRequest) {

        if (licensePlateProvinceRepository.existsByProvinceNameKh(licensePlateProvinceRequest.provinceNameKh().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Province name kh already exists!");
        }

        if (licensePlateProvinceRepository.existsByProvinceNameEnIgnoreCase(licensePlateProvinceRequest.provinceNameEn().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Province name en already exists!");
        }

        LicensePlateProvince licensePlateProvince = licensePlateProvinceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License plate province not found!")
        );
        licensePlateProvinceMapper.updateFromLicensePlateProvinceRequest(licensePlateProvinceRequest, licensePlateProvince);

        return licensePlateProvinceMapper.toLicensePlateProvinceResponse(licensePlateProvinceRepository.save(licensePlateProvince));
    }

    @Override
    public LicensePlateProvinceResponse create(LicensePlateProvinceRequest licensePlateProvinceRequest) {
        if (licensePlateProvinceRepository.existsByProvinceNameKh(licensePlateProvinceRequest.provinceNameKh().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Province name kh already exists!");
        }

        if (licensePlateProvinceRepository.existsByProvinceNameEnIgnoreCase(licensePlateProvinceRequest.provinceNameEn().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Province name en already exists!");
        }

        LicensePlateProvince licensePlateProvince = licensePlateProvinceMapper.fromLicensePlateProvinceRequest(licensePlateProvinceRequest);
        licensePlateProvince.setUuid(UUID.randomUUID().toString());
        licensePlateProvince.setCreatedAt(LocalDateTime.now());
        LicensePlateProvince licensePlateProvinceSaved = licensePlateProvinceRepository.save(licensePlateProvince);

        return licensePlateProvinceMapper.toLicensePlateProvinceResponse(licensePlateProvinceSaved);
    }

    @Override
    public List<LicensePlateProvinceResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<LicensePlateProvince> licensePlateProvince = licensePlateProvinceRepository.findAll(sort);
        return licensePlateProvince.stream().map(licensePlateProvinceMapper::toLicensePlateProvinceResponse).toList();
    }
}
