package edu.npic.sps.mapper;

import edu.npic.sps.domain.LicensePlateProvince;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceRequest;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LicensePlateProvinceMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromLicensePlateProvinceRequest(LicensePlateProvinceRequest licensePlateProvinceRequest, @MappingTarget LicensePlateProvince licensePlateProvince);

    LicensePlateProvince fromLicensePlateProvinceRequest(LicensePlateProvinceRequest licensePlateProvinceRequest);

    LicensePlateProvinceResponse toLicensePlateProvinceResponse(LicensePlateProvince licensePlateProvince);

}
