package edu.npic.sps.mapper;

import edu.npic.sps.domain.LicensePlateType;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LicensePlateTypeMapper{

    LicensePlateTypeResponse toLicensePlateTypeResponse(LicensePlateType licensePlateType);

}
