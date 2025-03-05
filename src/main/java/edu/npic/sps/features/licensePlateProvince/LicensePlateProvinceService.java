package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;

import java.util.List;

public interface LicensePlateProvinceService {
    List<LicensePlateProvinceResponse> findAll();
}
