package edu.npic.sps.mapper;

import edu.npic.sps.domain.LicensePlateType;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeRequest;
import edu.npic.sps.features.licensePlateType.dto.LicensePlateTypeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LicensePlateTypeMapper{
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromLicensePlateTypeRequest(LicensePlateTypeRequest licensePlateTypeRequest, @MappingTarget LicensePlateType licensePlateType);

    LicensePlateType fromLicensePlateTypeRequest(LicensePlateTypeRequest licensePlateTypeRequest);

    LicensePlateTypeResponse toLicensePlateTypeResponse(LicensePlateType licensePlateType);

}
