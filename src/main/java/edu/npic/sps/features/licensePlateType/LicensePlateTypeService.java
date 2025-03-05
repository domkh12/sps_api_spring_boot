package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;

import java.util.List;

public interface LicensePlateTypeService {
    List<LicensePlateTypeResponse> findAll();
}
