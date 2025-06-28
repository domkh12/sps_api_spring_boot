package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceRequest;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface LicensePlateProvinceService {
    void delete(String uuid);

    LicensePlateProvinceResponse updateByUuid(String uuid, @Valid LicensePlateProvinceRequest licensePlateProvinceRequest);

    LicensePlateProvinceResponse create(@Valid LicensePlateProvinceRequest licensePlateProvinceRequest);

    List<LicensePlateProvinceResponse> findAll();
}
