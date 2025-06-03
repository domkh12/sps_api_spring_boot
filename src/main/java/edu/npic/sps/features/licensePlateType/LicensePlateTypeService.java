package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeRequest;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;

import java.util.List;

public interface LicensePlateTypeService {
    void delete(String uuid);

    LicensePlateTypeResponse updateByUuid(String uuid, LicensePlateTypeRequest licensePlateTypeRequest);

    LicensePlateTypeResponse create(LicensePlateTypeRequest licensePlateTypeRequest);

    List<LicensePlateTypeResponse> findAll();
}
